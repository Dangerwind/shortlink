package com.shortlink.utils;

import com.shortlink.repository.LinkRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CalcRank {

    private final LinkRepository linkRepository;

    public void recalculateRank() {
        var links = linkRepository.findAllByOrderByCountDesc();
        long rank = 1;
        for(var oneLink : links) {
            oneLink.setRank(rank++);
        }
        linkRepository.saveAll(links);

    }

}
