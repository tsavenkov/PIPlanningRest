package com.mapper;

import com.domain.DomainSprint;
import com.domain.PiPlanning;
import com.model.outputModel.OutputModel;
import com.model.outputModel.OutputSprint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SolutionToOutputModel implements Serializable {
    public static OutputModel mapSolutionToOutputSprint(PiPlanning solution) {
        OutputModel outputModel = OutputModel.builder().build();
        List<OutputSprint> outputSprintList = new ArrayList<>();
        if (solution != null) {
            for (DomainSprint a : solution.getSprintList()) {
                OutputSprint outputSprint = OutputSprint.builder().build();
                outputSprint.setId(a.getId());
                outputSprint.setName(a.getName());

                outputSprint.setMaxBeCapacity(a.getMaxBeCapacity());
                outputSprint.setNonUsedBeCapacity(a.getUnusedBECapacity());
                outputSprint.setUsedBeCapacity(a.getMaxBeCapacity() - a.getUnusedBECapacity());

                outputSprint.setMaxFeCapacity(a.getMaxFeCapacity());
                outputSprint.setNonUsedFeCapacity(a.getUnusedFECapacity());
                outputSprint.setUsedFeCapacity(a.getMaxFeCapacity() - a.getUnusedFECapacity());

                outputSprint.setMaxSdCapacity(a.getMaxSdCapacity());
                outputSprint.setNonUsedSdCapacity(a.getUnusedSDCapacity());
                outputSprint.setUsedSdCapacity(a.getMaxSdCapacity() - a.getUnusedSDCapacity());

                outputSprint.setUserStoryList(solution.getUserStoryList().stream().filter(story -> (story.getSprint().getId() == outputSprint.getId())).collect(Collectors.toList()));
                outputSprintList.add(outputSprint);
            }
            outputModel.setOutputSprintList(outputSprintList);
        }

        outputModel.setTotalUnusedFECapacity(solution.getSprintList().stream().filter(domainSprint -> domainSprint.getId() != 6).mapToInt(DomainSprint::getUnusedFECapacity).sum());
        outputModel.setTotalUnusedBECapacity(solution.getSprintList().stream().filter(domainSprint -> domainSprint.getId() != 6).mapToInt(DomainSprint::getUnusedBECapacity).sum());
        outputModel.setTotalUnusedSDCapacity(solution.getSprintList().stream().filter(domainSprint -> domainSprint.getId() != 6).mapToInt(DomainSprint::getUnusedSDCapacity).sum());

        outputModel.setTotalUndistributedFECapacity(solution.getSprintList().stream().filter(domainSprint -> domainSprint.getId() == 6).mapToInt(DomainSprint::getMaxFeCapacity).sum() -
                solution.getSprintList().stream().filter(domainSprint -> domainSprint.getId() == 6).mapToInt(DomainSprint::getUnusedFECapacity).sum());
        outputModel.setTotalUndistributedBECapacity(solution.getSprintList().stream().filter(domainSprint -> domainSprint.getId() == 6).mapToInt(DomainSprint::getMaxBeCapacity).sum() -
                solution.getSprintList().stream().filter(domainSprint -> domainSprint.getId() == 6).mapToInt(DomainSprint::getUnusedBECapacity).sum());
        outputModel.setTotalUndistributedSDCapacity(solution.getSprintList().stream().filter(domainSprint -> domainSprint.getId() == 6).mapToInt(DomainSprint::getMaxSdCapacity).sum() -
                solution.getSprintList().stream().filter(domainSprint -> domainSprint.getId() == 6).mapToInt(DomainSprint::getUnusedSDCapacity).sum());

        outputModel.setScore(solution.getScore());

        //set partially planned features
        outputModel.setPartiallyPlannedFeatures(solution.getFeatureList().stream().filter(domainFeature ->
                solution.getUserStoryList().stream().anyMatch(domainUserStory -> domainUserStory.getSprint().getId() == 6 && domainFeature.getId() == domainUserStory.getFeature().getId())
                        &&
                        solution.getUserStoryList().stream().anyMatch(domainUserStory -> domainUserStory.getSprint().getId() != 6 && domainFeature.getId() == domainUserStory.getFeature().getId())

        ).collect(Collectors.toList()));

//        //set partially planned features
//        ArrayList<DomainFeature> partiallyPlanned = new ArrayList<>();
//
//        for (DomainFeature domainFeature : solution.getFeatureList()) {
//            if (solution.getUserStoryList().stream().anyMatch(domainUserStory -> domainUserStory.getSprint().getId() == 6 && domainFeature.getId() == domainUserStory.getFeature().getId()) &&
//                    solution.getUserStoryList().stream().anyMatch(domainUserStory -> domainUserStory.getSprint().getId() != 6 && domainFeature.getId() == domainUserStory.getFeature().getId()))
//                partiallyPlanned.add(domainFeature);
//
//        }
//        outputModel.setPartiallyPlannedFeatures(partiallyPlanned);
        return outputModel;
    }
}
