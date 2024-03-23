import React, {FormEvent, useCallback, useEffect, useState} from 'react';
import {Author, BookSaveDto, Genre, ValidationErrorDto} from "../types";
import {useNavigate} from "react-router-dom";
import {fileToBase64, getSelectInputValue} from "../utils";
import {getAuthors, getGenres, saveNewBook} from "../api";
import {Button, Col, Container, Form, Row} from "react-bootstrap";
import {AppLoader} from "../components/AppLoader/AppLoader";
import {ChapterCreateForm} from "../components/ChapterCreateForm/ChapterCreateForm";

function BookCreatePage() {
    const navigate = useNavigate();

    const [isLoading, setIsLoading] = useState<boolean>(true);
    const [authors, setAuthors] = useState<Author[] | null>(null);
    const [genres, setGenres] = useState<Genre[] | null>(null);
    const [error, setError] = useState<string[] | null>(null);
    const [formData, setFormData] = useState<BookSaveDto>({
        title: "",
        authorId: 1,
        genresIds: [],
        chapters: [],
        coverBase64: undefined
    })

    const navigateBack = useCallback(() => navigate("/"), [navigate]);

    const onSubmit = useCallback((e: FormEvent) => {
        e.preventDefault();
        saveNewBook(formData).then((response) => {
            if (response.ok) {
                navigateBack();
            } else if (response.status === 400) {
                response.json().then((errors: ValidationErrorDto) => {
                    setError(Object.values(errors));
                })
            } else {
                throw new Error("Что-то пошло не так");
            }
        } )
            .catch((error) => setError(error.message))
    }, [formData, navigateBack]);


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

    const addChapter = useCallback(() => {
        setFormData(prevState => ({...prevState, chapters: [
                ...prevState.chapters,
                {title: '', text: ''}
        ]}))
    }, []);


    const handleTextChange = useCallback((e: React.ChangeEvent<HTMLInputElement>) => {
        setFormData(prevState => ( {...prevState, [e.target.name]: e.target.value}));
    }, []);

    const handleFileChange = useCallback((e: React.ChangeEvent<HTMLInputElement>) => {
        const fileData = e.target.files?.[0];

        if (fileData) {
            fileToBase64(fileData).then((file) => {
                setFormData(prevState => ( {...prevState, [e.target.name]: file}));
            })
        }
    }, []);

    const handleChapterTextChange = useCallback((index: number, e: React.ChangeEvent<HTMLInputElement>) => {
        formData.chapters[index] = {
            ...formData.chapters[index],
            [e.target.name]: e.target.value
        };

        setFormData(prevState => ({
            ...prevState,
            chapters: formData.chapters
        }));
    }, [formData.chapters]);

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
                                <Form.Group controlId="formFile" className="mb-3">
                                    <Form.Label>Обложка (.jpg, .jpeg, .png)</Form.Label>
                                    <Form.Control type="file" name="coverBase64" onChange={handleFileChange}/>
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
                        <Row className='mt-4'>
                            <Col>
                                <Row className='d-flex flex-grow-0 justify-content-start'>
                                    <Col>
                                        <span className='fs-4'>
                                            Главы
                                        </span>
                                    </Col>
                                    <Col className='d-flex justify-content-end'>
                                        <Button variant='outline-secondary' size='sm'
                                                onClick={addChapter}>+ Добавить главу</Button>
                                    </Col>
                                </Row>
                                <Row>
                                    {formData.chapters.map((c, i) => (
                                        <ChapterCreateForm formData={c}
                                            onChange={(e) => handleChapterTextChange(i, e)} />
                                    ))}
                                </Row>
                            </Col>
                        </Row>
                        <Row className='mt-3'>
                            <Col className=''>
                                <Button variant="primary" type="submit">Сохранить</Button>
                                <Button className='m-lg-2' variant="outline-secondary"
                                        onClick={navigateBack}>Назад</Button>
                            </Col>
                        </Row>
                        {error && (
                            <div className="form-error">{
                                error.map(e => <>
                                    <span>{e}</span><br/>
                                </>)
                            }</div>
                        )}
                    </Container>
                </Form>
            ) : <AppLoader/> }
        </div>
    );
}

export default BookCreatePage;
