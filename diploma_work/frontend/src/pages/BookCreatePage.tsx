import React, {FormEvent, useCallback, useEffect, useState} from 'react';
import {Author, BookSaveDto, Genre} from "../types";
import {useNavigate} from "react-router-dom";
import {getSelectInputValue} from "../utils";
import {getAuthors, getGenres, saveNewBook} from "../api";
import {Button, Col, Container, Form, Row} from "react-bootstrap";
import {AppLoader} from "../components/AppLoader/AppLoader";

function BookCreatePage() {
    const navigate = useNavigate();

    const [isLoading, setIsLoading] = useState<boolean>(true);
    const [authors, setAuthors] = useState<Author[] | null>(null);
    const [genres, setGenres] = useState<Genre[] | null>(null);
    const [error, setError] = useState<string | null>(null);
    const [formData, setFormData] = useState<BookSaveDto>({
        title: "",
        authorId: 1,
        genresIds: []
    })

    const navigateBack = useCallback(() => navigate("/"), [navigate]);

    const onSubmit = useCallback((e: FormEvent) => {
        e.preventDefault();
        saveNewBook(formData).then((response) => {
            if (response.ok) {
                navigateBack();
            } else {
                throw new Error("Что-то пошло не так");
            }
        } )
            .catch((error) => setError(error.message))
    }, [formData]);


    useEffect(() => {
        Promise.all([
            getGenres(),
            getAuthors()
        ]).then(([genres, authors]) => {
            setAuthors(authors);
            setGenres(genres);
            setIsLoading(false);
        })
    }, [])


    const handleTextChange = useCallback((e: React.ChangeEvent<HTMLInputElement>) => {
        setFormData(prevState => ( {...prevState, [e.target.name]: e.target.value}));
    }, []);

    const handleSelectChange = useCallback((e: React.ChangeEvent<HTMLSelectElement>) => {
        setFormData(prevState => ( {...prevState, [e.target.name]: getSelectInputValue(e)}));
    }, []);

    return (
        <div>
            <h3>Создание книги</h3>
            {!isLoading ? (
                <Form onSubmit={onSubmit}
                      method="post"
                      className="form"
                >
                    <Container>
                        <Row>
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
            ) : <AppLoader/> }
        </div>
    );
}

export default BookCreatePage;
