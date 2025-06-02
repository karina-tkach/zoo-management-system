import { Route } from "react-router-dom";
import HomePage from "../pages/HomePage";
import LoginPage from "../pages/LoginPage";
import RegisterPage from "../pages/RegisterPage";
import AboutPage from "../pages/AboutPage";
import CancelPage from "../pages/CancelPage";
import SuccessPage from "../pages/SuccessPage";
import ErrorPage from "../pages/ErrorPage";
import TicketFormModal from "../pages/TicketFormModal.jsx";
import EventsView from "../pages/EventsView";
import EventDetailsPage from "../pages/EventDetailsPage";
import ExcursionsView from "../pages/ExcursionsView";
import AnimalsView from "../pages/AnimalsView.jsx";
import AnimalDetailsPage from "../pages/AnimalDetailsPage.jsx";

const PublicRoutes = [
        <Route path="/" element={<HomePage />} />,
        <Route path="/login" element={<LoginPage />} />,
        <Route path="/register" element={<RegisterPage />} />,
        <Route path="/about" element={<AboutPage />} />,

        <Route path="/view/events" element={<EventsView/>} />,
        <Route path="/view/events/:id" element={<EventDetailsPage/>} />,
        <Route path="/view/excursions" element={<ExcursionsView/>} />,

        <Route path="/view/animals" element={<AnimalsView/>} />,
        <Route path="/view/animals/:id" element={<AnimalDetailsPage/>} />,

        <Route path="/buy-ticket" element={<TicketFormModal visitType={"GENERAL"} />} />,
        <Route path="/cancel" element={<CancelPage />} />,
        <Route path="/success" element={<SuccessPage />} />,

        <Route path="/error" element={<ErrorPage />} />
];

export default PublicRoutes;
