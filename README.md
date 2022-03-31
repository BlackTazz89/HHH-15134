## Test case for HHH-15134

Trying to update a bytecode enhanced entity with a `@Version` annotated property, inline dirty tracking enabled and  
multiple `*ToMany` relations, yields to `ObjectOptimisticLockingFailure`.  

### Why this happens

If the entity has multiple `*ToMany` relations, then its `EntityMetamodel`'s [dynamicUpdate](https://github.com/hibernate/hibernate-orm/blob/5.6.5/hibernate-core/src/main/java/org/hibernate/tuple/entity/EntityMetamodel.java#L386) property evaluates `true` because more than one fetch group is found.  

If `dynamicUpdate` is `true` then `EnhancementAsProxyLazinessInterceptor` [won't initialize](https://github.com/hibernate/hibernate-orm/blob/5.6.5/hibernate-core/src/main/java/org/hibernate/bytecode/enhance/spi/interceptor/EnhancementAsProxyLazinessInterceptor.java#L76) the entity on _write_.  
This means that only the `SelfDirtinessTracker` will be used.    

After updating an entity field and returning from the `@Transactional` method the `TransactionInterceptor` will try to flush entities before committing.  
In order to decide if an update action should be added to the action queue, hibernate gets current entity state with [getPropertyValues](https://github.com/hibernate/hibernate-orm/blob/5.6.5/hibernate-core/src/main/java/org/hibernate/tuple/entity/AbstractEntityTuplizer.java#L559).    

In my opinion the problem is when the `getPropertyValues` method checks if [isAttributeLoaded](https://github.com/hibernate/hibernate-orm/blob/main/hibernate-core/src/main/java/org/hibernate/tuple/entity/BytecodeEnhancementMetadataPojoImpl.java#L119).  
I have the impression that this method is only aware of `LazyAttributeLoadingInterceptor` but in the test case an instance of `EnhancementAsProxyLazinessInterceptor` is used.  

Since mistakenly `isAttributeLoaded` returns `true`, a `GetterFieldImpl` is used to retrieve the value, de facto ignoring the interceptor code to load the entity.  
Hibernate assumes that all fields have been loaded and tries an update with version 0 causing `ObjectOptimisticLockingFailureException`.

### Reproduce with one click with maven wrapper

`./start.sh`

**Note**: Docker need because of _TestContainers_