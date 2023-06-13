package com.athmarine.service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.athmarine.entity.Currency;
import com.athmarine.entity.CurrencyEnum;
import com.athmarine.repository.CurrencyRepository;
import com.athmarine.request.CurrencyApiOblect;
import com.athmarine.request.ResponseData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Service
@EnableScheduling
public class CurrencyApiService {

	@Autowired
	RestTemplate restTemplate;

	@Value("${CURRENCY_URL}")
	private String CURRENCY_URL;

	@Value("${API_KEY}")
	private String API_KEY;

	@Autowired
	CurrencyRepository currencyRepository;

	public Object sendMessage() {

		Object object = null;

		ResponseEntity<String> urlResponse = restTemplate.getForEntity(CURRENCY_URL, String.class);

		Gson gson = new GsonBuilder().create();
		ResponseData reponse = gson.fromJson(urlResponse.getBody(), ResponseData.class);

		HashMap<String, Double> obj = new HashMap<>();
		obj.put(CurrencyEnum.aed.toString(), reponse.getRates().getAED());
		obj.put(CurrencyEnum.afn.toString(), reponse.getRates().getAFN());
		obj.put(CurrencyEnum.all.toString(), reponse.getRates().getALL());
		obj.put(CurrencyEnum.amd.toString(), reponse.getRates().getAMD());
		obj.put(CurrencyEnum.ang.toString(), reponse.getRates().getANG());
		obj.put(CurrencyEnum.aoa.toString(), reponse.getRates().getAOA());
		obj.put(CurrencyEnum.ars.toString(), reponse.getRates().getARS());
		obj.put(CurrencyEnum.aud.toString(), reponse.getRates().getAUD());
		obj.put(CurrencyEnum.awg.toString(), reponse.getRates().getAWG());
		obj.put(CurrencyEnum.azn.toString(), reponse.getRates().getAZN());
		obj.put(CurrencyEnum.bam.toString(), reponse.getRates().getBAM());
		obj.put(CurrencyEnum.bbd.toString(), reponse.getRates().getBBD());
		obj.put(CurrencyEnum.bch.toString(), reponse.getRates().getBCH());
		obj.put(CurrencyEnum.bdt.toString(), reponse.getRates().getBDT());
		obj.put(CurrencyEnum.bgn.toString(), reponse.getRates().getBGN());
		obj.put(CurrencyEnum.bhd.toString(), reponse.getRates().getBHD());
		obj.put(CurrencyEnum.bif.toString(), reponse.getRates().getBIF());
		obj.put(CurrencyEnum.bmd.toString(), reponse.getRates().getBMD());
		obj.put(CurrencyEnum.bnd.toString(), reponse.getRates().getBND());
		obj.put(CurrencyEnum.bob.toString(), reponse.getRates().getBOB());
		obj.put(CurrencyEnum.brl.toString(), reponse.getRates().getBRL());
		obj.put(CurrencyEnum.bsd.toString(), reponse.getRates().getBSD());
		obj.put(CurrencyEnum.btc.toString(), reponse.getRates().getBTC());
		obj.put(CurrencyEnum.btg.toString(), reponse.getRates().getBTG());
		obj.put(CurrencyEnum.bwp.toString(), reponse.getRates().getBWP());
		obj.put(CurrencyEnum.bzd.toString(), reponse.getRates().getBZD());
		obj.put(CurrencyEnum.cad.toString(), reponse.getRates().getCAD());
		obj.put(CurrencyEnum.cdf.toString(), reponse.getRates().getCDF());
		obj.put(CurrencyEnum.chf.toString(), reponse.getRates().getCHF());
		obj.put(CurrencyEnum.clp.toString(), reponse.getRates().getCLP());
		obj.put(CurrencyEnum.cnh.toString(), reponse.getRates().getCNH());
		obj.put(CurrencyEnum.cny.toString(), reponse.getRates().getCNY());
		obj.put(CurrencyEnum.cop.toString(), reponse.getRates().getCOP());
		obj.put(CurrencyEnum.crc.toString(), reponse.getRates().getCRC());
		obj.put(CurrencyEnum.cup.toString(), reponse.getRates().getCUP());
		obj.put(CurrencyEnum.cve.toString(), reponse.getRates().getCVE());
		obj.put(CurrencyEnum.czk.toString(), reponse.getRates().getCZK());
		obj.put(CurrencyEnum.dash.toString(), reponse.getRates().getDASH());
		obj.put(CurrencyEnum.djf.toString(), reponse.getRates().getDJF());
		obj.put(CurrencyEnum.dkk.toString(), reponse.getRates().getDKK());
		obj.put(CurrencyEnum.dop.toString(), reponse.getRates().getDOP());
		obj.put(CurrencyEnum.dzd.toString(), reponse.getRates().getDZD());
		obj.put(CurrencyEnum.egp.toString(), reponse.getRates().getEGP());
		obj.put(CurrencyEnum.eos.toString(), reponse.getRates().getEOS());
		obj.put(CurrencyEnum.etb.toString(), reponse.getRates().getETB());
		obj.put(CurrencyEnum.eth.toString(), reponse.getRates().getETH());
		obj.put(CurrencyEnum.eur.toString(), reponse.getRates().getEUR());
		obj.put(CurrencyEnum.fjd.toString(), reponse.getRates().getFJD());
		obj.put(CurrencyEnum.gbp.toString(), reponse.getRates().getGBP());
		obj.put(CurrencyEnum.gel.toString(), reponse.getRates().getGEL());
		obj.put(CurrencyEnum.ghs.toString(), reponse.getRates().getGHS());
		obj.put(CurrencyEnum.gip.toString(), reponse.getRates().getGIP());
		obj.put(CurrencyEnum.gmd.toString(), reponse.getRates().getGMD());
		obj.put(CurrencyEnum.gnf.toString(), reponse.getRates().getGNF());
		obj.put(CurrencyEnum.gtq.toString(), reponse.getRates().getGTQ());
		obj.put(CurrencyEnum.gyd.toString(), reponse.getRates().getGYD());
		obj.put(CurrencyEnum.hkd.toString(), reponse.getRates().getHKD());
		obj.put(CurrencyEnum.hnl.toString(), reponse.getRates().getHNL());
		obj.put(CurrencyEnum.hrk.toString(), reponse.getRates().getHRK());
		obj.put(CurrencyEnum.htg.toString(), reponse.getRates().getHTG());
		obj.put(CurrencyEnum.huf.toString(), reponse.getRates().getHUF());
		obj.put(CurrencyEnum.idr.toString(), reponse.getRates().getIDR());
		obj.put(CurrencyEnum.ils.toString(), reponse.getRates().getILS());
		obj.put(CurrencyEnum.inr.toString(), reponse.getRates().getINR());
		obj.put(CurrencyEnum.iqd.toString(), reponse.getRates().getIQD());
		obj.put(CurrencyEnum.irr.toString(), reponse.getRates().getIRR());
		obj.put(CurrencyEnum.isk.toString(), reponse.getRates().getISK());
		obj.put(CurrencyEnum.jmd.toString(), reponse.getRates().getJMD());
		obj.put(CurrencyEnum.jod.toString(), reponse.getRates().getJOD());
		obj.put(CurrencyEnum.jpy.toString(), reponse.getRates().getJPY());
		obj.put(CurrencyEnum.kes.toString(), reponse.getRates().getKES());
		obj.put(CurrencyEnum.kgs.toString(), reponse.getRates().getKGS());
		obj.put(CurrencyEnum.khr.toString(), reponse.getRates().getKHR());
		obj.put(CurrencyEnum.kmf.toString(), reponse.getRates().getKMF());
		obj.put(CurrencyEnum.krw.toString(), reponse.getRates().getKRW());
		obj.put(CurrencyEnum.kwd.toString(), reponse.getRates().getKWD());
		obj.put(CurrencyEnum.kyd.toString(), reponse.getRates().getKYD());
		obj.put(CurrencyEnum.kzt.toString(), reponse.getRates().getKZT());
		obj.put(CurrencyEnum.lak.toString(), reponse.getRates().getLAK());
		obj.put(CurrencyEnum.lbp.toString(), reponse.getRates().getLBP());
		obj.put(CurrencyEnum.lkr.toString(), reponse.getRates().getLKR());
		obj.put(CurrencyEnum.lrd.toString(), reponse.getRates().getLRD());
		obj.put(CurrencyEnum.lsl.toString(), reponse.getRates().getLSL());
		obj.put(CurrencyEnum.ltc.toString(), reponse.getRates().getLTC());
		obj.put(CurrencyEnum.lyd.toString(), reponse.getRates().getLYD());
		obj.put(CurrencyEnum.mad.toString(), reponse.getRates().getMAD());
		obj.put(CurrencyEnum.mdl.toString(), reponse.getRates().getMDL());
		obj.put(CurrencyEnum.mkd.toString(), reponse.getRates().getMKD());
		obj.put(CurrencyEnum.mmk.toString(), reponse.getRates().getMMK());
		obj.put(CurrencyEnum.mop.toString(), reponse.getRates().getMOP());
		obj.put(CurrencyEnum.mur.toString(), reponse.getRates().getMUR());
		obj.put(CurrencyEnum.mvr.toString(), reponse.getRates().getMVR());
		obj.put(CurrencyEnum.mwk.toString(), reponse.getRates().getMWK());
		obj.put(CurrencyEnum.mxn.toString(), reponse.getRates().getMXN());
		obj.put(CurrencyEnum.myr.toString(), reponse.getRates().getMYR());
		obj.put(CurrencyEnum.mzn.toString(), reponse.getRates().getMZN());
		obj.put(CurrencyEnum.nad.toString(), reponse.getRates().getNAD());
		obj.put(CurrencyEnum.ngn.toString(), reponse.getRates().getNGN());
		obj.put(CurrencyEnum.nio.toString(), reponse.getRates().getNIO());
		obj.put(CurrencyEnum.nok.toString(), reponse.getRates().getNOK());
		obj.put(CurrencyEnum.npr.toString(), reponse.getRates().getNPR());
		obj.put(CurrencyEnum.nzd.toString(), reponse.getRates().getNZD());
		obj.put(CurrencyEnum.omr.toString(), reponse.getRates().getOMR());
		obj.put(CurrencyEnum.pab.toString(), reponse.getRates().getPAB());
		obj.put(CurrencyEnum.pen.toString(), reponse.getRates().getPEN());
		obj.put(CurrencyEnum.pgk.toString(), reponse.getRates().getPGK());
		obj.put(CurrencyEnum.php.toString(), reponse.getRates().getPHP());
		obj.put(CurrencyEnum.pkr.toString(), reponse.getRates().getPKR());
		obj.put(CurrencyEnum.pln.toString(), reponse.getRates().getPLN());
		obj.put(CurrencyEnum.pyg.toString(), reponse.getRates().getPYG());
		obj.put(CurrencyEnum.qar.toString(), reponse.getRates().getQAR());
		obj.put(CurrencyEnum.ron.toString(), reponse.getRates().getRON());
		obj.put(CurrencyEnum.rsd.toString(), reponse.getRates().getRSD());
		obj.put(CurrencyEnum.rub.toString(), reponse.getRates().getRUB());
		obj.put(CurrencyEnum.rwf.toString(), reponse.getRates().getRWF());
		obj.put(CurrencyEnum.sar.toString(), reponse.getRates().getSAR());
		obj.put(CurrencyEnum.sbd.toString(), reponse.getRates().getSBD());
		obj.put(CurrencyEnum.scr.toString(), reponse.getRates().getSCR());
		obj.put(CurrencyEnum.sdg.toString(), reponse.getRates().getSDG());
		obj.put(CurrencyEnum.sek.toString(), reponse.getRates().getSEK());
		obj.put(CurrencyEnum.sgd.toString(), reponse.getRates().getSGD());
		obj.put(CurrencyEnum.sll.toString(), reponse.getRates().getSLL());
		obj.put(CurrencyEnum.sos.toString(), reponse.getRates().getSOS());
		obj.put(CurrencyEnum.srd.toString(), reponse.getRates().getSRD());
		obj.put(CurrencyEnum.svc.toString(), reponse.getRates().getSVC());
		obj.put(CurrencyEnum.szl.toString(), reponse.getRates().getSZL());
		obj.put(CurrencyEnum.thb.toString(), reponse.getRates().getTHB());
		obj.put(CurrencyEnum.tjs.toString(), reponse.getRates().getTJS());
		obj.put(CurrencyEnum.tmt.toString(), reponse.getRates().getTMT());
		obj.put(CurrencyEnum.tnd.toString(), reponse.getRates().getTND());
		obj.put(CurrencyEnum.top.toString(), reponse.getRates().getTOP());
		obj.put(CurrencyEnum.TRY.toString(), reponse.getRates().getTRY());
		obj.put(CurrencyEnum.ttd.toString(), reponse.getRates().getTTD());
		obj.put(CurrencyEnum.twd.toString(), reponse.getRates().getTWD());
		obj.put(CurrencyEnum.tzs.toString(), reponse.getRates().getTZS());
		obj.put(CurrencyEnum.uah.toString(), reponse.getRates().getUAH());
		obj.put(CurrencyEnum.ugx.toString(), reponse.getRates().getUGX());
		obj.put(CurrencyEnum.usd.toString(), reponse.getRates().getUSD());
		obj.put(CurrencyEnum.uyu.toString(), reponse.getRates().getUYU());
		obj.put(CurrencyEnum.uzs.toString(), reponse.getRates().getUZS());
		obj.put(CurrencyEnum.vnd.toString(), reponse.getRates().getVND());
		obj.put(CurrencyEnum.xaf.toString(), reponse.getRates().getXAF());
		obj.put(CurrencyEnum.xag.toString(), reponse.getRates().getXAG());
		obj.put(CurrencyEnum.xau.toString(), reponse.getRates().getXAU());
		obj.put(CurrencyEnum.xcd.toString(), reponse.getRates().getXCD());
		obj.put(CurrencyEnum.xlm.toString(), reponse.getRates().getXLM());
		obj.put(CurrencyEnum.xof.toString(), reponse.getRates().getXOF());
		obj.put(CurrencyEnum.xrp.toString(), reponse.getRates().getXRP());
		obj.put(CurrencyEnum.yer.toString(), reponse.getRates().getYER());
		obj.put(CurrencyEnum.zar.toString(), reponse.getRates().getZAR());
		obj.put(CurrencyEnum.zmw.toString(), reponse.getRates().getZMW());

		List<Currency> currency = currencyRepository.findAll();

		if (currency.size() == 0) {
			for (Map.Entry<String, Double> entry : obj.entrySet()) {
				currencyRepository.save(convertToEntity(entry.getKey(), entry.getValue()));
			}
		} else {
			for (Map.Entry<String, Double> entry : obj.entrySet()) {

				Calendar calendar = Calendar.getInstance();

				// get a java date (java.util.Date) from the Calendar instance.
				// this java date will represent the current date, or "now".
				java.util.Date currentDate = calendar.getTime();

				// now, create a java.sql.Date from the java.util.Date
				java.sql.Date date = new java.sql.Date(currentDate.getTime());

				Currency updateCurrency = currencyRepository.findByCurrency(entry.getKey()).orElseThrow(null);
				updateCurrency.setCurrency(entry.getKey());
				updateCurrency.setRate(entry.getValue());
				updateCurrency.setUpdatedAt(date);
				currencyRepository.save(updateCurrency);
			}

		}
		return object;

	}

	private Currency convertToEntity(String currency, Double rate) {

		return Currency.builder().currency(currency).rate(rate).build();

	}

	public List<CurrencyApiOblect> getAllCountryCurrency() {

		Currency currency71 = currencyRepository.findById(71).orElse(null);
		Currency currency112 = currencyRepository.findById(112).orElse(null);
		CurrencyApiOblect currencyApiOblect71 = new CurrencyApiOblect();
		currencyApiOblect71.setId(currency71.getId());
		currencyApiOblect71.setCurrency(currency71.getCurrency().toUpperCase());
		currencyApiOblect71.setCurrencySymbol(currency71.getCurrencySymbol());
		currencyApiOblect71.setRate(currency71.getRate());

		CurrencyApiOblect currencyApiOblect112 = new CurrencyApiOblect();
		currencyApiOblect112.setId(currency112.getId());
		currencyApiOblect112.setCurrency(currency112.getCurrency().toUpperCase());
		currencyApiOblect112.setCurrencySymbol(currency112.getCurrencySymbol());
		currencyApiOblect112.setRate(currency112.getRate());
		List<CurrencyApiOblect> currencyApiOblect = currencyRepository.findAllByOrderByCurrencyAsc().stream()
				.filter(currtency -> !currtency.getId().equals(71)).filter(currtency -> !currtency.getId().equals(112))
				.map(continents -> convertToModel(continents)).collect(Collectors.toList());
		currencyApiOblect.add(0, currencyApiOblect71);
		currencyApiOblect.add(1, currencyApiOblect112);

		return currencyApiOblect;
	}

	public CurrencyApiOblect convertToModel(Currency entity) {

		return CurrencyApiOblect.builder().id(entity.getId()).currency(entity.getCurrency().toUpperCase())
				.rate(entity.getRate()).currencySymbol(entity.getCurrencySymbol()).build();

	}

//	@Scheduled(fixedRate = 60000l)
//	public void demoServiceMethod() {
//		sendMessage();
//	}

}
