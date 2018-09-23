package com.jikeh.service;

import com.jikeh.mapper.AdMapper;
import com.jikeh.model.Ad;
import com.jikeh.mq.RabbitConfig;
import com.jikeh.mq.Sender;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdServiceImpl implements AdService {

	@Autowired
	private AdMapper adMapper;

	@Autowired
	private Sender sender;

	@Override
	public List<Ad> findByAd(Ad ad, Integer offset, Integer limit) {
		RowBounds rowBounds = RowBounds.DEFAULT;
		if(offset != null && limit != null){
			rowBounds = new RowBounds(offset, limit);
		}
		return adMapper.selectByAd(ad, rowBounds);
	}

	@Override
	public List<Ad> getAds() {
		return adMapper.selectAll();
	}

	@Override
	public Ad findById(Long id) {
		return adMapper.selectByPrimaryKey(id);
	}

	@Override
	public boolean saveOrUpdate(Ad ad) {
		if(ad.getId() == null){
			return adMapper.insert(ad) == 1;
		} else {
			return adMapper.updateById(ad) == 1;
		}
	}

	@Override
	public boolean deleteById(Long id) {
		if(id == null){
			throw new NullPointerException("id");
		}
		return adMapper.deleteById(id) == 1;
	}

	@Override
	public void sendMq(String content){
		sender.send(RabbitConfig.queueName, content);
	}

}
