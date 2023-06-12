package com.library.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.model.Rate;
import com.library.repository.RateRepository;
import com.library.service.RateService;

@Service
public class RateServiceImpl implements RateService{

	@Autowired
	private RateRepository rateRepository;

	@Override
	public Rate save(Rate entity) {
		return rateRepository.save(entity);
	}

	@Override
	public List<Rate> findByProduct(Long id) {
		return rateRepository.findByProduct(id);
	}
	
	
}
