package com.workshop.ETrade.Repository;

import com.workshop.ETrade.Persistance.Users.MemberDTO;
import org.springframework.data.mongodb.repository.MongoRepository;


//@NoRepositoryBean
public interface MemberRepository extends MongoRepository<MemberDTO, String> {

}
