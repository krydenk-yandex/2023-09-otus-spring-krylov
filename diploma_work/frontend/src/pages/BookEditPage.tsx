import React, {FormEvent, useCallback, useEffect, useState} from 'react';
import {Author, Book, BookSaveDto, Genre} from "../types";
import {useNavigate, useParams} from "react-router-dom";
import {getSelectInputValue} from "../utils";
import {getAuthors, getBookById, getGenres, updateBook} from "../api";
import {Button, Col, Container, Form, Row} from "react-bootstrap";
import {AppLoader} from "../components/AppLoader/AppLoader";

function BooksEditPage() {
    const [book, setBook] = useState<Book | null>(null);
    const [authors, setAuthors] = useState<Author[] | null>(null);
    const [genres, setGenres] = useState<Genre[] | null>(null);
    const [isLoading, setIsLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);
    const [formData, setFormData] = useState<BookSaveDto>({
        title: "",
        authorId: 0,
        genresIds: []
    })

    const navigate = useNavigate();

    let { bookId } = useParams();

    const navigateBack = useCallback(() => navigate("/"), [navigate]);

    const onSubmit = useCallback((e: FormEvent) => {
        e.preventDefault();
        updateBook(Number(bookId!), formData).then((response) => {
            if (response.ok) {
                navigateBack();
            }
            throw new Error("Что-то пошло не так");
        } )
            .catch((error) => setError(error.message))
    }, [bookId, formData, navigateBack]);

    const handleTextChange = useCallback((e: React.ChangeEvent<HTMLInputElement>) => {
        setFormData(prevState => ( {...prevState, [e.target.name]: e.target.value}));
    }, []);

    const handleSelectChange = useCallback((e: React.ChangeEvent<HTMLSelectElement>) => {
        setFormData(prevState => ( {...prevState, [e.target.name]: getSelectInputValue(e)}));
    }, []);

    useEffect(() => {
        Promise.all([
            getBookById(Number(bookId!)),
            getGenres(),
            getAuthors(),
        ]).then(([book, genres, authors]) => {
            setBook(book);
            setAuthors(authors);
            setGenres(genres);
            setIsLoading(false);

            setFormData({
                title: book.title,
                authorId: book.author.id,
                genresIds: book.genres.map((g: Genre) => g.id)
            });
        })
    }, [bookId])

    return (
        <div>
            <h3>Редактирование книги</h3>
            {!isLoading ? (
                <Form onSubmit={onSubmit}
                    method="post"
                    className="form"
                >
                    <Container>
                        <Row>
                            <Col>
                                <Form.Group className="mb-3" controlId="id-input">
                                    <Form.Label>ID</Form.Label>
                                    <Form.Control type="input" disabled value={book!.id}/>
                                </Form.Group>
                            </Col>
                            <Col>
                                <Form.Group className="mb-3" controlId="name-input">
                                    <Form.Label>Название</Form.Label>
                                    <Form.Control type="input" name="title"
                                                  value={formData.title} onChange={handleTextChange}/>
                                </Form.Group>
                            </Col>
                        </Row>
                        <Row>
                            <Col>
                                <Form.Group className="mb-3" controlId="genres-input">
                                    <Form.Label>Жанры</Form.Label>
                                    <Form.Select name="genresIds" multiple onChange={handleSelectChange}>
                                        {genres!.map(genre => (
                                            <option value={genre.id} key={genre.id}
                                                    selected={formData.genresIds.includes(genre.id)}>
                                                {genre.name}
                                            </option>
                                        ))}
                                    </Form.Select>
                                </Form.Group>
                            </Col>
                            <Col>
                                <Form.Group className="mb-3" controlId="author-input">
                                    <Form.Label>Автор</Form.Label>
                                    <Form.Select name="authorId" onChange={handleSelectChange}>
                                        {authors!.map(author => (
                                            <option value={author.id} key={author.id}
                                                    selected={author.id === formData.authorId}
                                                    label={author.fullName}
                                            />
                                        ))}
                                    </Form.Select>
                                </Form.Group>
                            </Col>
                        </Row>
                        {error && (
                            <div className="form-error">{error}</div>
                        )}
                        <Row>
                            <Col className=''>
                                <Button variant="primary" type="submit">Сохранить</Button>
                                <Button className='m-lg-2' variant="outline-secondary"
                                        onClick={navigateBack}>Назад</Button>
                            </Col>
                        </Row>
                    </Container>
                </Form>
            ) : <AppLoader/>}
        </div>
    );
}

export default BooksEditPage;
