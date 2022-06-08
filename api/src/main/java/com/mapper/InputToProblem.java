package com.mapper;

import com.domain.DomainFeature;
import com.domain.DomainSprint;
import com.domain.DomainUserStory;
import com.domain.PiPlanning;
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
                        .maxQACapacity(a.getMaxQaCapacity()!=null? a.getMaxQaCapacity().intValue(): 0)
                        .sharedCapacity(a.getSharedCapacity() != null ? a.getSharedCapacity().intValue() : 0)
                        .name(a.getName())
                        .build());

            int id = 0;
            for (Feature feature : inputModel.getFeatures()) {
                DomainFeature domainFeature = DomainFeature.builder()
                        .id(feature.getId().intValue())
                        .priority(feature.getPriority().intValue())
                        .subject(feature.getSubject())
                        .build();
                domainFeatures.add(domainFeature);

                for (UserStory b : feature.getUserStories())
                    if (b != null)
                        domainUserStories.add(DomainUserStory.builder()
                                .id(id++)
                                .beCapacity(b.getBeCapacity().intValue())
                                .feature(domainFeature)
                                .feCapacity(b.getFeCapacity().intValue())
                                .qaCapacity(b.getQaCapacity().intValue())
                                .befeTotalCapacity(b.getBeCapacity().intValue() + b.getFeCapacity().intValue())
                                .subject(b.getSubject())
                                .sdCapacity(b.getSdCapacity().intValue())
                                .fixedSprint(b.getFixedSprint() == null ? 0 : b.getFixedSprint())
                                .build());
            }
            piPlanning.setFeatureList(domainFeatures);
            piPlanning.setSprintList(domainSprints);
            piPlanning.setUserStoryList(domainUserStories);
        }
        return piPlanning;
    }
}
