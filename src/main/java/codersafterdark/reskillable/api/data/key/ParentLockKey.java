package codersafterdark.reskillable.api.data.key;

import codersafterdark.reskillable.api.data.RequirementHolder;
import codersafterdark.reskillable.base.LevelLockHandler;

public interface ParentLockKey extends LockKey {
    /**
     * Retrieves any sub requirements this key may have.
     * This usually can be implemented by calling {@link LevelLockHandler#getLocks(Class, Object[])}
     *
     * @return A RequirementHolder of the sub requirements.
     */
    RequirementHolder getSubRequirements();
}