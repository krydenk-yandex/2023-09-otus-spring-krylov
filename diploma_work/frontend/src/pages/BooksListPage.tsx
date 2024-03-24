import React, {useCallback, useEffect, useState} from 'react';
import {Book} from "../types";
import {useNavigate} from "react-router-dom";
import {deleteBook as deleteBookApi, getBooks} from "../api";
import {Button, Col, Container, Row} from "react-bootstrap";
import {AppLoader} from "../components/AppLoader/AppLoader";
import {BookCard} from "../components/BookCard/BookCard";
import {useIsAdmin} from "../hooks/useIsAdmin";

function BooksListPage() {
    const isAdmin = useIsAdmin();

    const [books, setBooks] = useState<Book[] | null>(null);

    const navigate = useNavigate();

    const navToCreate = useCallback(() => {
        navigate("/books/create");
    }, [navigate])

    useEffect(() => {
        getBooks()
            .then(books => {
                setBooks(books as Book[])
            });
    }, [])

    const deleteBook = useCallback((id: number) => {
        deleteBookApi(id).then(() => {
            setBooks(books!.filter(b => b.id !== id));
        })
    }, [books])

    return (
        <Container>
            <Row className='mb-3'>
                <Col className='col-10'>
                    <h2>Книги</h2>
                </Col>
                {isAdmin && (
                    <Col className='col-2'>
                        <Button variant='outline-primary' onClick={navToCreate}>
                            Создать книгу
                        </Button>
                    </Col>
                )}
            </Row>
            {books ? (
                books.map(book => <BookCard book={book} onDelete={deleteBook} />)
            ) : <AppLoader/>}
        </Container>
    );
}

export default BooksListPage;
