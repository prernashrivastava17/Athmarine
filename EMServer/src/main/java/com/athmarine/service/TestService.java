package com.athmarine.service;

import java.security.SecureRandom;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athmarine.entity.Test;
import com.athmarine.repository.TestRepository;
import com.athmarine.request.TestModel;

@Service
public class TestService {

	@Autowired
	TestRepository testRepository;

	public TestModel createTest(TestModel testModel) {

		Test tesObj = null;
		try {
			tesObj = testRepository.save(convertToEntity(testModel));
		} catch (Exception ex) {

			createTest(testModel);
		}
		return convertToModel(tesObj);
	}

	public static String createRandomCode(int codeLength) {

		char[] chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789".toCharArray();
		StringBuilder sb = new StringBuilder();
		Random random = new SecureRandom();
		for (int i = 0; i < codeLength; i++) {
			char c = chars[random.nextInt(chars.length)];
			sb.append(c);
		}
		String output = sb.toString();

		return output;
	}

	public Test convertToEntity(TestModel model) {

		return Test.builder().name(model.getName()).uid(createRandomCode(6)).build();

	}

	public TestModel convertToModel(Test entity) {
		return TestModel.builder().name(entity.getName()).build();
	}

	public String testDatabaseConnection() {

		return "test successful";
	}

}
