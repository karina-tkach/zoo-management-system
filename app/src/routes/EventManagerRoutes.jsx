import { Route } from "react-router-dom";
import ExcursionsPage from "../pages/ExcursionsPage.jsx";
import ExcursionForm from "../pages/ExcursionForm.jsx";
import EventsPage from "../pages/EventsPage.jsx";
import EventForm from "../pages/EventForm.jsx";
import ProtectedRoute from "./ProtectedRoute.jsx";

const EventManagerRoutes = [
        <Route path="/excursions" element={<ProtectedRoute requiredRoles={["EVENT_MANAGER"]}>
                <ExcursionsPage/>
        </ProtectedRoute>} />,
        <Route path="/excursions/add" element={<ProtectedRoute requiredRoles={["EVENT_MANAGER"]}>
                <ExcursionForm/>
        </ProtectedRoute>} />,
        <Route path="/excursions/edit/:id" element={<ProtectedRoute requiredRoles={["EVENT_MANAGER"]}>
                <ExcursionForm/>
        </ProtectedRoute>} />,

        <Route path="/events" element={<ProtectedRoute requiredRoles={["EVENT_MANAGER"]}>
                <EventsPage/>
        </ProtectedRoute>} />,
        <Route path="/events/add" element={<ProtectedRoute requiredRoles={["EVENT_MANAGER"]}>
                <EventForm/>
        </ProtectedRoute>} />,
        <Route path="/events/edit/:id" element={<ProtectedRoute requiredRoles={["EVENT_MANAGER"]}>
                <EventForm/>
        </ProtectedRoute>} />,
    ];

export default EventManagerRoutes;
