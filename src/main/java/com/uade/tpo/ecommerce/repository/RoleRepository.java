package com.uade.tpo.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.uade.tpo.ecommerce.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

  @Query(value = "select * from role where name = ?1", nativeQuery = true)
  List<Role> findByName(String name);

}
