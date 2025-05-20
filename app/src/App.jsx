import {Route, Routes} from "react-router-dom";
import HomePage from './pages/HomePage';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import Header from './components/Header';
import AboutPage from './pages/AboutPage'
import StaffPage from './pages/StaffPage';
import StaffForm from './pages/StaffForm';
import ExcursionsPage from './pages/ExcursionsPage';
import ExcursionForm from './pages/ExcursionForm';
import EventsPage from './pages/EventsPage';
import EventForm from './pages/EventForm';
import NotFound from './pages/NotFound';
import ErrorPage from './pages/ErrorPage';


const App = () => {

    return (
        <>
            <Header />
            <Routes>
                <Route path="/" element={<HomePage/>} />
                <Route path="/login" element={<LoginPage/>}/>
                <Route path="/register" element={<RegisterPage />} />
                <Route path="/about" element={<AboutPage/>} />
                <Route path="/staff" element={<StaffPage/>} />
                <Route path="/staff/add" element={<StaffForm/>} />
                <Route path="/staff/edit/:id" element={<StaffForm/>} />
                <Route path="/excursions" element={<ExcursionsPage/>} />
                <Route path="/excursions/add" element={<ExcursionForm/>} />
                <Route path="/excursions/edit/:id" element={<ExcursionForm/>} />
                <Route path="/events" element={<EventsPage/>} />
                <Route path="/events/add" element={<EventForm/>} />
                <Route path="/events/edit/:id" element={<EventForm/>} />
                <Route path="*" element={<NotFound/>} />
                <Route path="/error" element={<ErrorPage />} />
            </Routes>
        </>
    );
};

export default App;
