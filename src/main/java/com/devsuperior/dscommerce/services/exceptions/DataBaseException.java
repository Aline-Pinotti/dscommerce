package com.devsuperior.dscommerce.services.exceptions;

/*A RunTimeException não exige, em termos de comparação que se coloque o Try..Catch
 * Vamos usar o ControllerAdvice, que vai funcionar melhor com a RunTimeException
 * Obs. a Exception, por exemplo, exige que o try..catch seja implementado
*/
public class DataBaseException extends RuntimeException {

    public DataBaseException(String msg) {
        super(msg);
    }

}
