package codersafterdark.reskillable.api.requirement.logic;

import java.util.List;

import javax.annotation.Nonnull;

import codersafterdark.reskillable.api.requirement.Requirement;

public interface OuterRequirement {
    @Nonnull
    List<Class<? extends Requirement>> getInternalTypes();
}