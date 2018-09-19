package com.jikeh.service;

import com.jikeh.model.Ad;

import java.util.List;

public interface AdService {

	List<Ad> findByAd(Ad ad, Integer offset, Integer limit);

	Ad findById(Long id);

	boolean saveOrUpdate(Ad ad);

	boolean deleteById(Long id);
}
