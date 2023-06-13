package com.athmarine.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.athmarine.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	public User getUserByEmail(String email);

	public User getUserByUid(String uid);

	public User getUserByPrimaryPhone(String number);

	Boolean existsByEmail(String email);

	Boolean existsByPrimaryPhone(String phoneNumber);

	public Optional<User> getUserById(int id);

	public User getUserBycompanyId(User id);

	public List<User> findAllByListUser(Integer id);

	public List<User> findAllByApproverId(User id);

	public List<User> findAllByListUser(User companyId);

	public User findFirstByOrderByIdDesc();

	@Query(value = "SELECT * from user JOIN user_role ON user.id=user_role.user_id WHERE company_id=:id AND user_role.role_id=5", nativeQuery = true)
	public User getUserHeadByCompanyId(Integer id);

	@Query(value = "SELECT DISTINCT COUNT(*) from user JOIN user_role ON user.id=user_role.user_id WHERE company_id=:companyId AND user_role.role_id=3", nativeQuery = true)
	public Integer getAllEngineerByCompanyId(Integer companyId);
	
	@Query(value = "SELECT DISTINCT COUNT(*) from user JOIN user_role ON user.id=user_role.user_id WHERE company_id=:companyId AND user_role.role_id=2", nativeQuery = true)
	public Integer getAllBidderByCompanyId(Integer companyId);
	
	@Query(value = "SELECT DISTINCT COUNT(*) from user JOIN user_role ON user.id=user_role.user_id WHERE company_id=:companyId AND user_role.role_id=1", nativeQuery = true)
	public Integer getAllAdminByCompanyId(Integer companyId);
	
	@Query(value = "SELECT DISTINCT COUNT(*) from user JOIN user_role ON user.id=user_role.user_id WHERE company_id=:companyId AND user_role.role_id=5", nativeQuery = true)
	public Integer getAllVendorHeadByCompanyId(Integer companyId);
	
	@Query(value = "SELECT DISTINCT COUNT(*) from user JOIN user_role ON user.id=user_role.user_id WHERE company_id=:companyId AND user_role.role_id=4", nativeQuery = true)
	public Integer getAllVendorApproverByCompanyId(Integer companyId);

	@Query(value = "SELECT DISTINCT COUNT(*) from user JOIN user_role ON user.id=user_role.user_id WHERE company_id=:companyId AND user_role.role_id=6", nativeQuery = true)
	public Integer getAllFinancerByCompanyId(Integer companyId);
	
	@Query(value = "SELECT * from user JOIN user_role ON user.id=user_role.user_id WHERE company_id=:companyId AND user_role.role_id=6", nativeQuery = true)
	public List<User> getAllFinaneEmailAndPhoneNumberByCompanyId(Integer companyId);
	
	@Query(value = "SELECT * from user JOIN user_role ON user.id=user_role.user_id WHERE company_id=:companyId AND user_role.role_id=3", nativeQuery = true)
	public List<User> getAllEngineersByCompanyId(Integer companyId);
	
	@Query(value = "SELECT * from user JOIN user_role ON user.id=user_role.user_id WHERE company_id=:companyId AND user_role.role_id=6", nativeQuery = true)
	public List<User> getAllFinanceByCompanyId(Integer companyId);
	
	@Query(value = "SELECT DISTINCT COUNT(*) from user JOIN user_role ON user.id=user_role.user_id WHERE company_id=:companyId AND user_role.role_id=8", nativeQuery = true)
	public Integer getAllRequestorByCompanyId(Integer companyId);

	@Query(value = "SELECT * from user JOIN user_role ON user.id=user_role.user_id WHERE company_id=:companyId AND user_role.role_id=2", nativeQuery = true)
	public List<User> getAllBiddersByCompanyId(Integer companyId);
}