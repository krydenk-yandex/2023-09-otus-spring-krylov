import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import BooksListPage from "./pages/BooksListPage";
import BookEditPage from "./pages/BookEditPage";
import BookCreatePage from "./pages/BookCreatePage";

const router = createBrowserRouter([
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
]);

const root = ReactDOM.createRoot(
  document.getElementById('root') as HTMLElement
);

root.render(
  <React.StrictMode>
      <RouterProvider router={router} />
  </React.StrictMode>
);
