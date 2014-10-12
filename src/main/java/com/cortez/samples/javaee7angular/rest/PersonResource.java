package com.cortez.samples.javaee7angular.rest;

import com.cortez.samples.javaee7angular.data.Person;
import com.cortez.samples.javaee7angular.pagination.PaginatedListWrapper;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * REST Service to expose the data to display in the UI grid.
 *
 * @author Roberto Cortez
 */
@Stateless
//@ApplicationPath("/resources")
@Path("persons")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/persons", description = "The persons manager")
public class PersonResource extends Application {
    @PersistenceContext
    private EntityManager entityManager;

    private Integer countPersons() {
        Query query = entityManager.createQuery("SELECT COUNT(p.id) FROM Person p");
        return ((Long) query.getSingleResult()).intValue();
    }

    @SuppressWarnings("unchecked")
    private List<Person> findPersons(int startPosition, int maxResults, String sortFields, String sortDirections) {
        Query query =
                entityManager.createQuery("SELECT p FROM Person p ORDER BY p." + sortFields + " " + sortDirections);
        query.setFirstResult(startPosition);
        query.setMaxResults(maxResults);
        return query.getResultList();
    }

    private PaginatedListWrapper findPersons(PaginatedListWrapper wrapper) {
        wrapper.setTotalResults(countPersons());
        int start = (wrapper.getCurrentPage() - 1) * wrapper.getPageSize();
        wrapper.setList(findPersons(start,
                                    wrapper.getPageSize(),
                                    wrapper.getSortFields(),
                                    wrapper.getSortDirections()));
        return wrapper;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "List all the persons",
            notes = "Ask for all the persons paginating the results")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = PaginatedListWrapper.class),
            @ApiResponse(code = 500, message = "Something wrong in Server")})
    public Response listPersons(@DefaultValue("1")
                                            @QueryParam("page")
                                            Integer page,
                                            @DefaultValue("id")
                                            @QueryParam("sortFields")
                                            String sortFields,
                                            @DefaultValue("asc")
                                            @QueryParam("sortDirections")
                                            String sortDirections) {
        PaginatedListWrapper paginatedListWrapper = new PaginatedListWrapper();
        paginatedListWrapper.setCurrentPage(page);
        paginatedListWrapper.setSortFields(sortFields);
        paginatedListWrapper.setSortDirections(sortDirections);
        paginatedListWrapper.setPageSize(10);
        return Response.status(200).entity(findPersons(paginatedListWrapper)).build();
    }

    @GET
    @Path("/{id}")
    @ApiOperation(value = "Get a person data",
            notes = "Ask for all the details of a person")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Person.class),
            @ApiResponse(code = 500, message = "Something wrong in Server")})
    public Response getPerson(@PathParam("id") Long id) {
        Person p = entityManager.find(Person.class, id);
        return Response.status(200).entity(p).build();
    }

    @POST
    @ApiOperation(value = "Save or Update a person",
            notes = "Save the person data if no ID is in the entity or update it using the ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Person.class),
            @ApiResponse(code = 500, message = "Something wrong in Server")})
    public Response savePerson(Person person) {
        if (person.getId() == null) {
            Person personToSave = new Person();
            personToSave.setName(person.getName());
            personToSave.setDescription(person.getDescription());
            personToSave.setImageUrl(person.getImageUrl());
            entityManager.persist(person);
        } else {
            Response personToUpdateResp = getPerson(person.getId());
            Person personToUpdate = (Person) personToUpdateResp.getEntity();
            personToUpdate.setName(person.getName());
            personToUpdate.setDescription(person.getDescription());
            personToUpdate.setImageUrl(person.getImageUrl());
            person = entityManager.merge(personToUpdate);
        }

        return Response.status(200).entity(person).build();
    }

    @DELETE
    @Path("/{id}")
    @ApiOperation(value = "Delete a given person",
            notes = "The server will delete the person of the given ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "Something wrong in Server")})
    public Response deletePerson(@PathParam("id") Long id) {
        entityManager.remove(getPerson(id).getEntity());
        return Response.status(200).build();
    }
}
