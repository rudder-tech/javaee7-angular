package com.cortez.samples.javaee7angular.pagination;

import com.cortez.samples.javaee7angular.data.Person;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

/**
 * Wraps all the information needed to paginate a table.
 *
 * @author Roberto Cortez
 */
@ApiModel(value = "PaginatedListWrapper", description = "A person in the company")
@XmlRootElement
public class PaginatedListWrapper implements Serializable {

    /**
     * This is a comment on the property
     *
     * @value: currentPage2
     * @notes: This is an example
     */
    @ApiModelProperty(value = "currentPage", notes = "The page we want to get from the results")
    private Integer currentPage;

    @ApiModelProperty(value = "pageSize", notes = "How many items we want per page")
    private Integer pageSize;

    @ApiModelProperty(value = "totalResults", notes = "The total number of results returned by the query")
    private Integer totalResults;

    @ApiModelProperty(value = "sortFields", notes = "The fields used to sort the results")
    private String sortFields;

    @ApiModelProperty(value = "sortDirections", notes = "The directions of the sort for each field used in the sort of the query")
    private String sortDirections;


    @XmlElement
    @ApiModelProperty(value = "list", notes = "The persons in the current page")
    private List<Person> list;

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    public String getSortFields() {
        return sortFields;
    }

    public void setSortFields(String sortFields) {
        this.sortFields = sortFields;
    }

    public String getSortDirections() {
        return sortDirections;
    }

    public void setSortDirections(String sortDirections) {
        this.sortDirections = sortDirections;
    }

    public List getList() {
        return list;
    }

    public void setList(List<Person> list) {
        this.list = list;
    }
}
