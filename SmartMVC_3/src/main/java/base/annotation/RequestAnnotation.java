package base.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/**
 * 请求注解  
 * @author lenovo
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestAnnotation {
	public String value();
}
