package com.shortlink.controller;

import com.shortlink.mapper.LinkMapper;
import com.shortlink.model.Link;
import com.shortlink.repository.LinkRepository;

import dto.LinkFullDTO;
import dto.LinkInputDTO;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.shortlink.utils.Coder.decode;
import static com.shortlink.utils.Coder.encode;
import com.shortlink.utils.CalcRank;



@RestController
@AllArgsConstructor
public class LinksController {

    //   @Autowired
    private LinkRepository linkRepository;
    // @Autowired
    private LinkMapper linkMapper;

    private final CalcRank calcRank;

    @PostMapping(path = "/generate")
    @ResponseStatus(HttpStatus.CREATED)
    public LinkInputDTO generate(@RequestBody LinkInputDTO linkInputDTO) {
        var link = linkMapper.map(linkInputDTO);
        var existing = linkRepository.findByOriginal(link.getOriginal());
        if (existing.isPresent()) {
            var got = existing.get();
            got.setLink("/l/" + got.getLink());
            got.setOriginal(null);
            return linkMapper.map(got);
        }
        link.setCount(0L);
        link.setRank(0L);
        link = linkRepository.save(link);
        var codeLink = encode(link.getId());
        link.setLink(codeLink);

        var ret = linkMapper.map(linkRepository.save(link));
        ret.setLink("/l/" + codeLink);
        ret.setOriginal(null);
        return ret;
    }

    @GetMapping(path = "/l/{link}")
    public void getLink(@PathVariable("link") String shortLink, HttpServletResponse response) throws IOException {
        var dataID = decode(shortLink);
        if (dataID == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Некорректная ссылка");
        }

        var link = linkRepository.findById(dataID)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Ссылка не найдена или устарела"));

        link.setCount(link.getCount() + 1L);
        linkRepository.save(link);

        response.sendRedirect(link.getOriginal());
    }

    @GetMapping(path = "/stats/{link}")
    public LinkFullDTO getLinkStats(@PathVariable("link") String shortLink) {
        calcRank.recalculateRank();
        var dataID = decode(shortLink);
        if (dataID == null) {
            return null;
        }
            var ret = linkRepository.findById(dataID)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Ссылка не найдена или устарела"));


        var retDTO = linkMapper.mapFull(ret);
        retDTO.setLink("/l/" + retDTO.getLink());
        return retDTO;
    }

    @GetMapping(path = "/stats")
    public List<LinkFullDTO> getLinkList(
            @RequestParam(name = " и ", required = false, defaultValue = "0") int page,
            @RequestParam(name = "count", required = false, defaultValue = "10") int count) {
        if (count > 100) { count = 100; }
        calcRank.recalculateRank();
        var links = linkRepository.findAllByOrderByCountDesc();
        var linksOut = links.stream()
                .map(link -> {
                    var fillDTO = linkMapper.mapFull(link);
                    fillDTO.setLink("/l/" + fillDTO.getLink());
                    return fillDTO;
                    }
                )
                .toList();
        return linksOut;
    }
}
/*

 надо сделать фронтэнд
API моего бэкенда такое

POST запрос на "/generate" в теле запроса JSON с полем "original" : "ссылка"
GET запрос на "/l/{link}" где link - это сокрашенная ссылка
GET запрос на "/stats/{link}" где link - это сокрашенная ссылка
GET запрос на "/stats" с параметрами page и count

что должен делать фроненд:

Главная страница, оглавление "Генератор сокращенных ссылок". В центре окна браузера строка ввода с подписью "введите ссылку для сокращения". Рядом кнопка "сократить"
Так же сверху есть ссылки-навигация "проверить короткую ссылку" и "посмотреть статистику ссылок"

Если на главной страницу в строку ввода ввести любую ссылку и начать "enter" или кнопку "сократить" то фронтэнд делает моему бэкенду POST запрос в теле с JSON полем "original" : "и тут то что написали в строке ввода" и получит в ответ тоже JSON с полем "link" : "и вот тут будет сокращенная ссылка" и эту сокращенную ссылку надо вывести на экран и написать "Вот ваша сокращенная ссылка".
Если нажать на "проверить короткую ссылку" но фронтэнд запрашивает сокращенную ссылку, и делает GET запрос моему бэкенду "/stats/{link}" где link это и есть эта введенная сокращенная ссылка. В ответ мой бэкенд прислывает JSON с полями
String link, String original, Long rank, Long count
И надо вывести: Ваша сокращенная ссылка link ведет на ресурс original ее рейтинг rank и по ней перешли count раз.

Если нажать на "посмотреть статистику ссылок" то фронтенд спросит "выводить по "count" строк начиная со страницу "page"  и после ввода этих данных сделает GET запрос на "/stats" с этими параметрами page и count и в ответ получит JSON массив в полями String link, String original, Long rank, Long count и их надо вывести списком и предусмотреть кнопки "следующая страницу" "предыдущая страницу" "первая страница" если строк меньше чем count то переход по "следующая страница" не делать.

И везде должна быть кнопка "домой" чтобы перейти в к самой начальной станице с запросом о сокращении ссылки.

Сделай красиво с применением bootstrap чтобы окошки ввода были с закрегленными краями и чтобы все было не просто черно-белое а с применением цветов. Ориентируйся как сделан поисковик Google.

пришли zip файл с этим бкэнтом, он будет применяться в Java SpringBoot проекте и размещен в папке static

 */