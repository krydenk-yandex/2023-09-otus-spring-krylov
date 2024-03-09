import {createBrowserRouter, RouterProvider} from "react-router-dom";
import BooksListPage from "./pages/BooksListPage";
import BookEditPage from "./pages/BookEditPage";
import BookCreatePage from "./pages/BookCreatePage";
import React, {FC} from "react";
import {User} from "./types";
import {LoginPage} from "./pages/LoginPage";

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
    }
];

const nonAuthorizedRoutes = [
    {
        path: "/",
        element: <LoginPage />,
    }
];

export const Router: FC<Props> = ({user}) => (
    <RouterProvider router={createBrowserRouter(
        user ? authorizedRoutes : nonAuthorizedRoutes
    )} />
);