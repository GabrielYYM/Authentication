import axios from "axios";

export const apiClient = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || "http://localhost:8080",
  headers: {
    "Content-Type": "application/json",
  },
});

apiClient.interceptors.request.use((config) => {
  const token = localStorage.getItem("authToken");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export async function login(email, password) {
  const { data } = await apiClient.post("/auth/login", { email, password });
  if (data?.token) {
    localStorage.setItem("authToken", data.token);
    document.cookie = `authToken=${data.token}; path=/; max-age=86400; SameSite=Lax`;
  }
  if (data?.email) {
    localStorage.setItem("userEmail", data.email);
  }
  if (data?.role) {
    localStorage.setItem("userRole", data.role);
  }
  return data;
}

export async function register(userData) {
  const { data } = await apiClient.post("/auth/register", {
    email: userData.email,
    password: userData.password,
    role: userData.role || "CUSTOMER",
  });
  return data;
}

export async function logout() {
  const token = localStorage.getItem("authToken");
  try {
    if (token) {
      await apiClient.post("/auth/logout");
    }
  } catch (error) {
    console.warn("Erro ao invalidar token no backend:", error);
  } finally {
    localStorage.removeItem("authToken");
    localStorage.removeItem("userEmail");
    localStorage.removeItem("userRole");
    document.cookie = "authToken=; path=/; max-age=0; SameSite=Lax";
  }
}

export function getCurrentUser() {
  const token = localStorage.getItem("authToken");
  const email = localStorage.getItem("userEmail");
  const role = localStorage.getItem("userRole");
  if (!token) return null;
  return { token, email, role };
}

export async function fetchRoleDashboard(rolePath) {
  const { data } = await apiClient.get(`/${rolePath}/dashboard`);
  return data;
}
