package com.mapper;

import com.domain.DomainFeature;
import com.domain.DomainUserStory;
import com.domain.PiPlanning;
import com.domain.DomainSprint;
import com.model.inputModel.Feature;
import com.model.inputModel.Planning;
import com.model.inputModel.Sprint;
import com.model.inputModel.UserStory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class InputToProblem implements Serializable {
    public static PiPlanning mapInputToProblem(Planning inputModel) {
        PiPlanning piPlanning = PiPlanning.builder().build();
        if (inputModel != null) {
            List<DomainSprint> domainSprints = new ArrayList<>();
            List<DomainFeature> domainFeatures = new ArrayList<>();
            List<DomainUserStory> domainUserStories = new ArrayList<>();
            for (Sprint a : inputModel.getSprints())
                domainSprints.add(DomainSprint.builder()
                        .id(a.getId())
                        .maxBeCapacity(a.getMaxBeCapacity().intValue())
                        .maxFeCapacity(a.getMaxFeCapacity().intValue())
                        .maxSdCapacity(a.getMaxSdCapacity().intValue())
                        .name(a.getName())
                        .build());

            for (Feature a : inputModel.getFeatures())
                domainFeatures.add(DomainFeature.builder()
                        .id(a.getId().intValue())
                        .priority(a.getPriority().intValue())
                        .subject(a.getSubject())
                        .build());

            for (UserStory a : inputModel.getUserStories())
                domainUserStories.add(DomainUserStory.builder()
                                .id(a.getId().intValue())
                                .beCapacity(a.getBeCapacity().intValue())
                                .feature(domainFeatures.stream().filter(b -> a.getFeatureId() == b.getId()).findFirst().get())
                                .feCapacity(a.getFeCapacity().intValue())
                                .subject(a.getSubject())
                                .sdCapacity(a.getSdCapacity().intValue())
                        .build());
            piPlanning.setFeatureList(domainFeatures);
            piPlanning.setSprintList(domainSprints);
            piPlanning.setUserStoryList(domainUserStories);
        }
        return piPlanning;
    }
}
