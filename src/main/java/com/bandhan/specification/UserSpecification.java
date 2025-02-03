package com.bandhan.specification;

import org.springframework.data.jpa.domain.Specification;

import com.bandhan.entity.User;
import com.bandhan.enums.UserStatus;

public class UserSpecification {

    public static Specification<User> hasId(Long id) {
        return (root, query, criteriaBuilder) -> {
            if(id == null)
                return criteriaBuilder.conjunction();
            
            return criteriaBuilder.equal(root.get("id"), id);
        };
    }

    public static Specification<User> firstNameContains(String firstName) {
        return (root, query, criteriaBuilder) -> {
            if(firstName == null || firstName.isEmpty())
                return criteriaBuilder.conjunction();
            
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), "%" + firstName.toLowerCase() + "%");
        };
    }

    public static Specification<User> lastNameContains(String lastName) {
        return (root, query, criteriaBuilder) -> {
            if(lastName == null || lastName.isEmpty())
                return criteriaBuilder.conjunction();
            
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), "%" + lastName.toLowerCase() + "%");
        };
    }

    public static Specification<User> hasMobile(String mobile) {
        return (root, query, criteriaBuilder) -> {
            if(mobile == null || mobile.isEmpty())
                return criteriaBuilder.conjunction();
            
            return criteriaBuilder.equal(root.get("mobile"), mobile);
        };
    }

    public static Specification<User> hasEmail(String email) {
        return (root, query, criteriaBuilder) -> {
            if(email == null || email.isEmpty())
                return criteriaBuilder.conjunction();
            
            return criteriaBuilder.equal(root.get("email"), email);
        };
    }

    public static Specification<User> addressContains(String address) {
        return (root, query, criteriaBuilder) -> {
            if(address == null || address.isEmpty())
                return criteriaBuilder.conjunction();
            
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("address")), "%" + address.toLowerCase() + "%");
        };
    }

    public static Specification<User> hasStatus(UserStatus userStatus) {
        return (root, query, criteriaBuilder) -> {
            if(userStatus == null)
                return criteriaBuilder.conjunction();
            
            return criteriaBuilder.equal(root.get("userStatus"), userStatus);
        };
    }
}
