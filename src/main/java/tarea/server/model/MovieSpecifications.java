package tarea.server.model;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Predicate;
import java.util.Objects;

public class MovieSpecifications implements Specification<Movie> {

    private SearchCriteria criteria;

    public MovieSpecifications(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    public SearchCriteria getCriteria() {
        return criteria;
    }

    @Override
    public Predicate toPredicate(Root<Movie> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if(Objects.isNull(criteria)){
            return null;
        }
        switch (criteria.getOperation()) {
            case "Equals":
                return builder.equal(root.get(criteria.getKey()), criteria.getValue());
            case "Contains":
                return builder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
            case "Greater Than":
                return builder.greaterThanOrEqualTo(root.get(criteria.getKey()),  criteria.getValue());
            default:
                return null;
        }
    }
}