package com.cumulocity.rest.representation.jsonpredicate;

import lombok.*;
import org.svenson.JSONProperty;
import org.svenson.JSONTypeHint;

import java.util.LinkedList;
import java.util.List;


/**
 * Represents a predicate to be verified on a json.
 * It can represent a leaf predicate when using a non-conjunction operations (EQ, NEQ, GT, LT, GTE, LTE, IN). In this form requires
 * {@code parameterPath} and {@code value}.
 * It can represent a branch predicate when using a conjunction operator (AND, OR). In this form it requires
 * {@code childPredicates}
 */
@Data
@NoArgsConstructor
public class JSONPredicateRepresentation {

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private String operator;

    /**
     * path to parameter inside json
     */
    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private String parameterPath;

    /**
     * value used in comparison, with operator specified
     */
    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private String value;

    /**
     * child predicates in case of composite predicate
     */
    @Getter(onMethod_ = {
            @JSONProperty(ignoreIfNull = true),
            @JSONTypeHint(value = JSONPredicateRepresentation.class)})
    @Setter(onMethod_ = {
            @JSONProperty(ignoreIfNull = true),
            @JSONTypeHint(value = JSONPredicateRepresentation.class)})
    @Singular
    private List<JSONPredicateRepresentation> childPredicates = new LinkedList<>();

    @Builder(builderMethodName = "jsonLeafPredicateRepresentation", buildMethodName = "buildLeaf")
    public JSONPredicateRepresentation(String operator, String parameterPath, String value) {
        this.operator = operator;
        this.parameterPath = parameterPath;
        this.value = value;
    }

    @Builder(builderMethodName = "jsonBranchPredicateRepresentation", buildMethodName = "buildBranch")
    public JSONPredicateRepresentation(String operator, String parameterPath, @Singular List<JSONPredicateRepresentation> childPredicates) {
        this.operator = operator;
        this.parameterPath = parameterPath;
        this.childPredicates = childPredicates;
    }
}
