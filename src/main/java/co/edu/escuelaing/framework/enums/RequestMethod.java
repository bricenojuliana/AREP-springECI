package co.edu.escuelaing.framework.enums;

/**
 * {@code RequestMethod} is an enumeration that represents the HTTP methods supported by the framework.
 * Currently, only {@code GET} is supported, but other HTTP methods like {@code POST}, {@code PUT}, and {@code DELETE}
 * can be added as needed.
 */
public enum RequestMethod {
    /**
     * Represents the HTTP GET method.
     * Used to retrieve data from the server.
     */
    GET,

    // Uncomment the following lines to support additional HTTP methods
    // /**
    //  * Represents the HTTP POST method.
    //  * Used to submit data to be processed to a specified resource.
    //  */
    // POST,

    // /**
    //  * Represents the HTTP PUT method.
    //  * Used to update a resource or create a new resource if it does not exist.
    //  */
    // PUT,

    // /**
    //  * Represents the HTTP DELETE method.
    //  * Used to delete a resource from the server.
    //  */
    // DELETE
}
