package plus.xyc.server.wiki.book.entity.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import plus.xyc.server.wiki.book.entity.dto.Book;
import plus.xyc.server.wiki.book.entity.request.CreateBookRequest;
import plus.xyc.server.wiki.book.entity.response.BookResponse;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookMapStruct {

    Book toBook(CreateBookRequest request);
    BookResponse toResponse(Book book);

}
