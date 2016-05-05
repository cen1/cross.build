package build.cross.rest.v1.application;

import com.github.tfaga.lynx.utils.QueryStringDefaults;
import java.util.Optional;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/v1")
public class CrossBuildRest extends Application {

	public static final String BASE_URL = Optional.ofNullable(System.getenv("BASE_URL")).orElse("http://localhost:8080") + "/v1";

    public static final Integer QUERY_DEFAULT_LIMIT = 50;
    public static final Integer QUERY_DEFAULT_OFFSET = 0;
    public static final Integer QUERY_MAX_LIMIT = 100;

    public static final QueryStringDefaults queryDefaults = new QueryStringDefaults()
            .maxLimit(QUERY_MAX_LIMIT)
            .defaultLimit(QUERY_DEFAULT_LIMIT)
            .defaultOffset(QUERY_DEFAULT_OFFSET);
}
