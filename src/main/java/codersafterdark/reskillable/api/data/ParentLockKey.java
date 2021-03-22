package codersafterdark.reskillable.api.data;

import codersafterdark.reskillable.common.core.handler.LevelLockHandler;

public interface ParentLockKey extends LockKey {
    /**
     * Retrieves any sub requirements this key may have.
     * This usually can be implemented by calling {@link LevelLockHandler#getLocks(Class, Object[])}
     *
     * @return A RequirementHolder of the sub requirements.
     */
    RequirementHolder getSubRequirements();
}