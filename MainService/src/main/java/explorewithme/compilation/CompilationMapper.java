package explorewithme.compilation;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.List;
import java.util.stream.Collectors;

public class CompilationMapper extends ModelMapper {

    public CompilationMapper() {
        setUp();
    }

    public <A, T> List<T> mapCompilationList(List<A> list, Class<T> destinationClass) {
        return list.stream()
                .map(compilation -> super.map(compilation, destinationClass))
                .collect(Collectors.toList());
    }

    private void setUp() {
        this.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }
}
