package com.library.service;

import java.util.List;

import com.library.model.Rate;

public interface RateService {

	Rate save(Rate entity);

	List<Rate> findByProduct(Long id);

}
