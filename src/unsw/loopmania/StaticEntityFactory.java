package unsw.loopmania;

public interface StaticEntityFactory {
    public StaticEntity createStaticEntity(Class<?> entityClass, LoopManiaWorld world);
    public StaticEntity createRandomStaticEntity(LoopManiaWorld world);
}
