package plus.xyc.server.wiki.book.entity.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import plus.xyc.server.wiki.book.entity.dto.Book;
import plus.xyc.server.wiki.book.entity.request.CreateBookRequest;
import plus.xyc.server.wiki.book.entity.response.BookResponse;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookMapStruct {

    Book toBook(CreateBookRequest request);
    BookResponse toResponse(Book book);

}
