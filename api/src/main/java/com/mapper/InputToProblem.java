package com.mapper;

import com.domain.*;
import com.model.inputModel.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InputToProblem implements Serializable {
    public static PiPlanning mapInputToProblem(Planning inputModel) {
        PiPlanning piPlanning = PiPlanning.builder().build();
        if (inputModel != null) {
            List<DomainSprint> domainSprints = new ArrayList<>();
            List<DomainFeature> domainFeatures = new ArrayList<>();
            List<DomainUserStory> domainUserStories = new ArrayList<>();
            List<DomainChampion> domainChampions = new ArrayList<>();
            List<DomainChampionInSprint> domainChampionInSprintList = new ArrayList<>();

            for (InputSprint a : inputModel.getSprints()) {
                DomainSprint domainSprint = DomainSprint.builder()
                        .id(a.getId())
                        .maxBeCapacity(a.getMaxBeCapacity().intValue())
                        .maxFeCapacity(a.getMaxFeCapacity().intValue())
                        .maxSdCapacity(a.getMaxSdCapacity().intValue())
                        .sharedCapacity(a.getSharedCapacity() != null ? a.getSharedCapacity().intValue() : 0)
                        .name(a.getName())
                        .build();


                //todo add champions creation
                domainSprint.setDomainChampionInSprintList(getChampionsForDomainSprints(a));
                domainSprints.add(domainSprint);
            }

            domainChampions = getChampions(inputModel.getSprints());

            int id = 0;
            for (InputFeature feature : inputModel.getFeatures()) {
                DomainFeature domainFeature = DomainFeature.builder()
                        .id(feature.getId().intValue())
                        .priority(feature.getPriority().intValue())
                        .subject(feature.getSubject())
                        .build();
                domainFeatures.add(domainFeature);

                for (InputUserStory b : feature.getUserStories())
                    if (b != null)
                        domainUserStories.add(DomainUserStory.builder()
                                .id(id++)
                                .beCapacity(b.getBeCapacity().intValue())
                                .feature(domainFeature)
                                .feCapacity(b.getFeCapacity().intValue())
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

    private static List<DomainChampion> getChampions(List<InputSprint> sprints) {
        //we form the array of all champions to be then assignes to a user story and added with the constraints
        List<String> champCodes = new ArrayList<>();
        List<DomainChampion> returnList = new ArrayList<>();

        sprints.forEach(inputSprint ->
        {
            List<String> names = new ArrayList<>();
            if (inputSprint.getChampions() != null)
                names = inputSprint.getChampions().stream().map(InputChampion::getName).collect(Collectors.toList());
            names.forEach(name -> {
                if (!champCodes.contains(name)) champCodes.add(name);
            });


        });

        champCodes.forEach(code -> returnList.add(DomainChampion.builder().name(code).build()));
        return returnList;
    }


    private static List<DomainChampionInSprint> getChampionsForDomainSprints(InputSprint sprint) {
        List<DomainChampionInSprint> returnList = new ArrayList<>();
        if (sprint.getChampions() == null)
            return null;
        sprint.getChampions().forEach(inputChampion -> returnList.add(DomainChampionInSprint.builder()
                .capacity(inputChampion.getCapacity())
                .name(inputChampion.getName())
                .capaType(inputChampion.getCapaType()).build()));
        return returnList;
    }
}
