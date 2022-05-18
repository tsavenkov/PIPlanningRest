package com.mapper;

import com.domain.DomainSprint;
import com.domain.DomainUserStory;
import com.domain.PiPlanning;
import com.model.outputModel.*;

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


    public static OutputModelUSView mapSolutionToOutputUserStories(PiPlanning solution) {
        OutputModelUSView outputModel = OutputModelUSView.builder().build();
        List<OutputUserStory> outputUserStories = new ArrayList<>();
        if (solution != null) {
            for (DomainUserStory a : solution.getUserStoryList()) {
                OutputUserStory outputUserStory = OutputUserStory.builder().build();
                outputUserStory.setFeatureName(a.getFeature().getSubject());
                outputUserStory.setCapacity(a.getBeCapacity() + a.getSdCapacity() + a.getFeCapacity());   //todo: change to separate capacities. at the moment works since most of the cases only cap exists for the user
                // story
                if (a.getSprint().getId() == 1)
                    outputUserStory.setSprint1name(a.getSubject());
                else if (a.getSprint().getId() == 2)
                    outputUserStory.setSprint2name(a.getSubject());
                else if (a.getSprint().getId() == 3)
                    outputUserStory.setSprint3name(a.getSubject());
                else if (a.getSprint().getId() == 4)
                    outputUserStory.setSprint4name(a.getSubject());
                else if (a.getSprint().getId() == 5)
                    outputUserStory.setSprint5name(a.getSubject());
                else if (a.getSprint().getId() == 6)
                    outputUserStory.setOutOfScopeName(a.getSubject());
                outputUserStories.add(outputUserStory);
            }
            outputModel.setOutputUserStories(outputUserStories);
            outputModel.setSprintsCaps(new ArrayList<>());

            /// get the capacities of the sprints
            for (DomainSprint sprint : solution.getSprintList()) {
                USCapacity usCapacity = USCapacity.builder()
                        .id(sprint.getId())
                        .maxFeCapacity(sprint.getMaxFeCapacity())
                        .maxBeCapacity(sprint.getMaxBeCapacity())
                        .maxSdCapacity(sprint.getMaxSdCapacity())
                        .nonUsedFeCapacity(sprint.getUnusedFECapacity())
                        .nonUsedBeCapacity(sprint.getUnusedBECapacity())
                        .nonUsedSdCapacity(sprint.getUnusedSDCapacity())
                        .usedFeCapacity(sprint.getMaxFeCapacity() - sprint.getUnusedFECapacity())
                        .usedBeCapacity(sprint.getMaxBeCapacity() - sprint.getUnusedBECapacity())
                        .usedSdCapacity(sprint.getMaxSdCapacity() - sprint.getUnusedSDCapacity()).build();
                outputModel.getSprintsCaps().add(usCapacity);
            }
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


        return outputModel;
    }

}
