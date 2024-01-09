package com.library.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.model.Member;
import com.library.repository.MemberRepository;

@Service
public class MemberService {

	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private LoanService loanService;
	
	public Long getTotalCount() {
		return memberRepository.count();
	}
	
	public List<Member> getAll() {
		return memberRepository.findAllByOrderByFirstNameAscMiddleNameAscLastNameAsc();
	}
	
	public Member get(Long id) {
		return memberRepository.findById(id).get();
	}
	
	public Member addNew(Member member) {
		member.setJoiningDate( new Date() );
		return memberRepository.save( member );
	}
	
	public Member save(Member member) {
		return memberRepository.save( member );
	}
	
	public void delete(Member member) {
		memberRepository.delete(member);
	}
	
	public void delete(Long id) {
		memberRepository.deleteById(id);
	}
	
	public boolean hasUsage(Member member) {
		return loanService.getCountByMember(member) > 0;
	}
	
}
