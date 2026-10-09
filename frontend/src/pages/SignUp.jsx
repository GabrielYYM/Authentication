import { useState } from "react";
import { Mail, Lock, ShieldCheck, Eye, EyeOff, UserCheck } from "lucide-react";
import { register } from "../services/authService";

export default function SignUp({ onNavigate }) {
  const [form, setForm] = useState({
    email: "",
    password: "",
    confirmPassword: "",
    role: "CUSTOMER",
  });

  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    if (!form.email || !form.password || !form.confirmPassword) {
      return setError("Por favor, preencha todos os campos.");
    }

    if (form.password.length < 6) {
      return setError("A senha deve ter no mínimo 6 caracteres.");
    }

    if (form.password !== form.confirmPassword) {
      return setError("As senhas não coincidem.");
    }

    setLoading(true);
    try {
      await register({
        email: form.email,
        password: form.password,
        role: form.role,
      });

      if (onNavigate) {
        onNavigate("Login");
      }
    } catch (err) {
      const responseData = err?.response?.data;
      if (typeof responseData === "string") {
        setError(responseData);
      } else if (Array.isArray(responseData)) {
        setError(responseData.map((item) => item.mensagem || item.defaultMessage || item).join(", "));
      } else if (responseData?.message) {
        setError(responseData.message);
      } else {
        setError("Erro ao criar conta.");
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-[#171522] flex items-center justify-center p-4">
      <div className="w-full max-w-md bg-[#1e1c2a] rounded-2xl border border-white/10 shadow-2xl overflow-hidden">
        <div className="px-8 pt-8 pb-6 text-center border-b border-white/10">
          <div className="w-16 h-16 bg-amber-500/20 text-amber-500 rounded-full flex items-center justify-center mx-auto mb-4">
            <ShieldCheck size={32} />
          </div>
          <h1 className="text-2xl font-bold text-white mb-2">Criar Conta</h1>
          <p className="text-sm text-zinc-400">
            Cadastre-se e selecione seu perfil de acesso
          </p>
        </div>

        <form onSubmit={handleSubmit} className="p-8 space-y-4">
          {error && (
            <div className="p-3 bg-red-500/10 border border-red-500/20 rounded-xl text-red-400 text-sm text-center">
              {error}
            </div>
          )}

          <div>
            <label className="block text-zinc-400 text-sm mb-2">Email</label>
            <div className="relative">
              <Mail size={18} className="absolute left-4 top-1/2 -translate-y-1/2 text-zinc-500" />
              <input
                type="email"
                name="email"
                value={form.email}
                onChange={handleChange}
                placeholder="seu@email.com"
                required
                className="w-full rounded-xl bg-white/5 border border-white/10 pl-11 pr-4 py-3 text-sm text-zinc-200 placeholder:text-zinc-600 outline-none focus:border-amber-500/50 focus:ring-1 focus:ring-amber-500/30 transition-colors"
              />
            </div>
          </div>

          <div>
            <label className="block text-zinc-400 text-sm mb-2">Perfil (Role)</label>
            <div className="relative">
              <UserCheck size={18} className="absolute left-4 top-1/2 -translate-y-1/2 text-zinc-500" />
              <select
                name="role"
                value={form.role}
                onChange={handleChange}
                className="w-full rounded-xl bg-[#232032] border border-white/10 pl-11 pr-4 py-3 text-sm text-zinc-200 outline-none focus:border-amber-500/50 focus:ring-1 focus:ring-amber-500/30 transition-colors cursor-pointer"
              >
                <option value="CUSTOMER">Cliente</option>
                <option value="SELLER">Vendedor</option>
              </select>
            </div>
          </div>

          <div>
            <label className="block text-zinc-400 text-sm mb-2">Senha</label>
            <div className="relative">
              <Lock size={18} className="absolute left-4 top-1/2 -translate-y-1/2 text-zinc-500" />
              <input
                type={showPassword ? "text" : "password"}
                name="password"
                value={form.password}
                onChange={handleChange}
                placeholder="•••••••• (mínimo 6 caracteres)"
                required
                className="w-full rounded-xl bg-white/5 border border-white/10 pl-11 pr-11 py-3 text-sm text-zinc-200 placeholder:text-zinc-600 outline-none focus:border-amber-500/50 focus:ring-1 focus:ring-amber-500/30 transition-colors"
              />
              <button
                type="button"
                onClick={() => setShowPassword((prev) => !prev)}
                className="absolute right-4 top-1/2 -translate-y-1/2 text-zinc-500 hover:text-zinc-300 transition-colors cursor-pointer"
                aria-label={showPassword ? "Ocultar senha" : "Exibir senha"}
              >
                {showPassword ? <EyeOff size={18} /> : <Eye size={18} />}
              </button>
            </div>
          </div>

          <div>
            <label className="block text-zinc-400 text-sm mb-2">Confirmar Senha</label>
            <div className="relative">
              <Lock size={18} className="absolute left-4 top-1/2 -translate-y-1/2 text-zinc-500" />
              <input
                type={showConfirmPassword ? "text" : "password"}
                name="confirmPassword"
                value={form.confirmPassword}
                onChange={handleChange}
                placeholder="••••••••"
                required
                className="w-full rounded-xl bg-white/5 border border-white/10 pl-11 pr-11 py-3 text-sm text-zinc-200 placeholder:text-zinc-600 outline-none focus:border-amber-500/50 focus:ring-1 focus:ring-amber-500/30 transition-colors"
              />
              <button
                type="button"
                onClick={() => setShowConfirmPassword((prev) => !prev)}
                className="absolute right-4 top-1/2 -translate-y-1/2 text-zinc-500 hover:text-zinc-300 transition-colors cursor-pointer"
                aria-label={showConfirmPassword ? "Ocultar senha" : "Exibir senha"}
              >
                {showConfirmPassword ? <EyeOff size={18} /> : <Eye size={18} />}
              </button>
            </div>
          </div>

          <button
            type="submit"
            disabled={loading}
            className="w-full bg-amber-500 hover:bg-amber-400 disabled:opacity-50 disabled:cursor-not-allowed transition-colors text-zinc-900 font-medium rounded-xl px-5 py-3.5 text-sm cursor-pointer mt-4"
          >
            {loading ? "Criando conta..." : "Criar conta"}
          </button>

          <div className="text-center mt-4">
            <p className="text-sm text-zinc-400">
              Já tem uma conta?{" "}
              <button
                type="button"
                onClick={() => onNavigate && onNavigate("Login")}
                className="text-amber-500 hover:text-amber-400 font-medium transition-colors cursor-pointer"
              >
                Faça login
              </button>
            </p>
          </div>
        </form>
      </div>
    </div>
  );
}
