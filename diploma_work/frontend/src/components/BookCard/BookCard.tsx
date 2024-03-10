import {Button, Col, Container, Row} from "react-bootstrap";
import {FC, useCallback} from "react";
import {Book} from "../../types";

import css from './BookCard.module.css';
import {useNavigate} from "react-router-dom";
import {useIsAdmin} from "../../hooks/useIsAdmin";

type Props = {
    book: Book
    onDelete: (id: number) => void
}

export const BookCard: FC<Props> = ({book, onDelete}) => {
    const isAdmin = useIsAdmin();

    const navigate = useNavigate();

    const navToEdit = useCallback(() => {
        navigate(`/books/edit/${book.id}`);
    }, [book.id, navigate])

    const navToRead = useCallback(() => {
        navigate(`/books/${book.id}`);
    }, [book.id, navigate])

    return (
        <Container className={css.bookCard}>
            <Row>
                <Col xs={3}>
                    <img
                        className={css.bookImage}
                        src={'/logo.png'}
                        alt={"Book"}
                    />
                </Col>
                <Col xs={6}>
                    <Row className='fs-5 fw-bold mb-2'>
                        <Col>
                            {book.title}
                        </Col>
                    </Row>
                    <Row>
                        <Col>
                            <span className='fw-bold'>Автор:</span> {book.author.fullName}
                        </Col>
                    </Row>
                    <Row>
                        <Col>
                            <span className='fw-bold'>Жанры:</span> {book.genres.map(g => g.name).join(", ")}
                        </Col>
                    </Row>
                </Col>
                <Col xs={3} className='d-flex flex-column gap-2 align-items-end'>
                    <Button variant='primary' size={'lg'} onClick={navToRead}>
                        Читать
                    </Button>
                    {isAdmin && (
                        <>
                            <Button variant='outline-secondary' size={'sm'} onClick={navToEdit}>
                                Редактировать
                            </Button>
                            <Button variant='outline-danger' size={'sm'} onClick={() => onDelete(book.id)}>
                                Удалить
                            </Button>
                        </>
                    )}
                </Col>
            </Row>
        </Container>
    )
}