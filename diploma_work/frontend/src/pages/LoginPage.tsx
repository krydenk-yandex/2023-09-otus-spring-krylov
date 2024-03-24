import React, {FC, FormEvent, useCallback, useState} from "react";
import {Button, Form, Stack} from "react-bootstrap";
import {UserAuthDto} from "../types";
import {authenticate} from "../api";

export const LoginPage: FC = () => {
    const handleTextChange = useCallback((e: React.ChangeEvent<HTMLInputElement>) => {
        setFormData(prevState => ({
            ...prevState,
            [e.target.name]: e.target.value
        }));
    }, []);

    const [formData, setFormData] = useState<UserAuthDto>({
        username: "",
        password: "",
    });

    const onSubmit = useCallback((e: FormEvent) => {
        e.preventDefault();
        authenticate(formData).then((response) => {
            if (response.ok) {
                response.text().then(token => {
                    localStorage.setItem("accessToken", token);
                    // eslint-disable-next-line
                    location.reload();
                });
            } else {
                throw new Error("Что-то пошло не так");
            }
        })
    }, [formData]);

    return (
        <Stack gap={2} className="col-md-5 mx-auto">
            <h2>Авторизация</h2>
            <Form onSubmit={onSubmit}>
                <Form.Group className="mb-3" controlId="login">
                    <Form.Label>Логин</Form.Label>
                    <Form.Control type="input" placeholder="Логин" name="username"
                                  value={formData?.username} onChange={handleTextChange}/>
                </Form.Group>
                <Form.Group className="mb-3" controlId="password">
                    <Form.Label>Пароль</Form.Label>
                    <Form.Control type="password" placeholder="Пароль" name="password"
                                  value={formData?.password} onChange={handleTextChange}/>
                </Form.Group>
                <Button variant="primary" type="submit">
                    Войти
                </Button>
            </Form>
        </Stack>
    )
}