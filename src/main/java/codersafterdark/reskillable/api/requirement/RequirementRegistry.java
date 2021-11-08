package codersafterdark.reskillable.api.requirement;

import java.util.Map;

import org.apache.logging.log4j.Level;

import com.google.common.collect.Maps;

import codersafterdark.reskillable.api.ReskillableRegistries;
import codersafterdark.reskillable.api.skill.Skill;
import codersafterdark.reskillable.common.Reskillable;
import net.minecraft.util.ResourceLocation;

public class RequirementRegistry {
    private Map<String, RequirementFunction<String, Requirement>> requirementHandlers = Maps.newHashMap();

    public Requirement getRequirement(String requirementString) {
        String[] requirements = requirementString.split("\\|");
        Requirement requirement = null;
        try {
            if (requirements.length == 2) {
                String requirementType = requirements[0];
                String requirementInputs = requirements[1];

                if (this.requirementHandlers.containsKey(requirementType)) {
                    requirement = this.requirementHandlers.get(requirementType).apply(requirementInputs);
                } else {
                    Skill skill = ReskillableRegistries.SKILLS.getValue(new ResourceLocation(requirementType));
                    if (skill == null) {
                        throw new RequirementException("Skill '" + requirementType + "' not found.");
                    }
                    try {
                        int level = Integer.parseInt(requirementInputs);
                        if (level > 1) {
                            requirement = new SkillRequirement(skill, level);
                        } else {
                            throw new RequirementException("Level must be greater than 1. Found: '" + level + "'.");
                        }
                    } catch (NumberFormatException e) {
                        throw new RequirementException("Invalid level '" + requirementInputs + "' for skill '" + skill.getName() + "'.");
                    }
                }
            } else if (requirements.length > 0) {
                String requirementType = requirements[0];
                if (this.requirementHandlers.containsKey(requirementType)) {
                    //Pass them the whole extended requirement Inputs (Note: they will have to split by | themselves)
                    int pos = requirementType.length() + 1;
                    String input = pos > requirementString.length() ? "" : requirementString.substring(pos);
                    requirement = this.requirementHandlers.get(requirementType).apply(input);
                }
            }
        } catch (RequirementException e) {
            Reskillable.logger.log(Level.ERROR, "Requirement Format Exception (" + requirementString + "): " + e.getMessage());
            return null;
        }
        if (requirement == null) {
            //Fall back for if the requirement does not throw a detailed error message
            Reskillable.logger.log(Level.ERROR, "No Requirement found for Input: " + requirementString);
        } else if (!requirement.isEnabled()) {
            Reskillable.logger.log(Level.ERROR, "Disabled Requirement for Input: " + requirementString);
            return null;
        }
        return requirement;
    }

    public void addRequirementHandler(String identity, RequirementFunction<String, Requirement> creator) {
        this.requirementHandlers.put(identity, creator);
    }
}