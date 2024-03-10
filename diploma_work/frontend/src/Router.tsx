import {createBrowserRouter, RouterProvider} from "react-router-dom";
import BooksListPage from "./pages/BooksListPage";
import BookEditPage from "./pages/BookEditPage";
import BookCreatePage from "./pages/BookCreatePage";
import React, {FC} from "react";
import {User} from "./types";
import {LoginPage} from "./pages/LoginPage";
import {ChapterReadPage} from "./pages/ChapterReadPage";
import {BookPage} from "./pages/BookPage";

type Props = {
    user: User | null
}

const authorizedRoutes = [
    {
        path: "/",
        element: <BooksListPage/>,
    },
    {
        path: "/books/edit/:bookId",
        element: <BookEditPage/>,
    },
    {
        path: "/books/create",
        element: <BookCreatePage/>,
    },
    {
        path: "/books/:bookId",
        element: <BookPage />,
    },
    {
        path: "/chapters/:uuid",
        element: <ChapterReadPage />,
        exact: false
    }
];

const nonAuthorizedRoutes = [
    {
        path: "*",
        element: <LoginPage />,
    }
];

export const Router: FC<Props> = ({user}) => (
    <RouterProvider router={createBrowserRouter(
        user ? authorizedRoutes : nonAuthorizedRoutes
    )} />
);