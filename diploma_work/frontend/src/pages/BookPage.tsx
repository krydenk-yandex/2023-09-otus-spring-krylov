import React, {FC, useCallback, useEffect, useState} from 'react';
import {useNavigate, useParams} from "react-router-dom";
import {Button, Col, Container, Row} from "react-bootstrap";
import {Book} from "../types";
import {getBookById} from "../api";
import {useIsAdmin} from "../hooks/useIsAdmin";
import {AppLoader} from "../components/AppLoader/AppLoader";

import css from "./BookPage.module.css";
import {ChaptersList} from "../components/СhaptersList/ChaptersList";

export const BookPage: FC = () => {
    const navigate = useNavigate();
    const isAdmin = useIsAdmin();
    let { bookId } = useParams();

    const [book, setBook] = useState<Book | null>(null);

    const navToEdit = useCallback(() => {
        navigate(`/books/edit/${book?.id}`);
    }, [book, navigate])

    const navToMain = useCallback(() => {
        navigate(`/books`);
    }, [navigate])

    useEffect(() => {
        getBookById(Number(bookId!)).then((book) => setBook(book));
    }, [book, bookId])

    return book ? (
        <Container>
            <Row>
                <Col xs={4}>
                    <img
                        className={css.bookImage}
                        src={'/logo.png'}
                        alt={"Book"}
                    />
                </Col>
                <Col xs={5}>
                    <Row className='fs-3 fw-bold mb-2'>
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
                    <Row>
                        <Col className={'mt-5'}>
                            <span className='fw-bold fs-5'>Содержание:</span>
                            <ChaptersList bookId={Number(bookId!!)} />
                        </Col>
                    </Row>
                </Col>
                <Col xs={3} className='d-flex flex-column gap-2 align-items-end'>
                    {isAdmin && (
                        <>
                            <Button variant='outline-secondary' size={'sm'} onClick={navToEdit}>
                                Редактировать
                            </Button>
                            <Button variant='outline-danger' size={'sm'} onClick={navToMain}>
                                Удалить
                            </Button>
                        </>
                    )}
                </Col>
            </Row>
        </Container>
    ) : (
        <AppLoader/>
    )
}