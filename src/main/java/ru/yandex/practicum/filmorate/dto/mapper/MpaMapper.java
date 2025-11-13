package ru.yandex.practicum.filmorate.dto.mapper;

import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.dto.MpaDto;

public class MpaMapper {

    public static MpaDto toDto(MpaRating mpa) {
        if (mpa == null) return null;
        MpaDto dto = new MpaDto();
        dto.setId(mpa.getId());
        dto.setName(mpa.getName());
        dto.setDescription(mpa.getDescription());
        return dto;
    }

    public static MpaRating toEntity(MpaDto dto) {
        if (dto == null) return null;
        MpaRating mpa = new MpaRating();
        mpa.setId(dto.getId());
        mpa.setName(dto.getName());
        mpa.setDescription(dto.getDescription());
        return mpa;
    }
}
