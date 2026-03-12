/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        cta: '#06B6D4',
        'cta-hover': '#0891B2',
        accent: '#3B82F6',
      },
      fontFamily: {
        heading: ['Righteous', 'sans-serif'],
        body: ['Poppins', 'sans-serif']
      },
      animation: {
        'fade-in': 'fadeIn 0.5s ease-out',
        'slide-up': 'slideUp 0.5s ease-out',
        'glow-pulse': 'glowPulse 2s ease-in-out infinite',
      },
      keyframes: {
        fadeIn: {
          '0%': { opacity: '0' },
          '100%': { opacity: '1' },
        },
        slideUp: {
          '0%': { opacity: '0', transform: 'translateY(20px)' },
          '100%': { opacity: '1', transform: 'translateY(0)' },
        },
        glowPulse: {
          '0%, 100%': { boxShadow: '0 0 15px rgba(6, 182, 212, 0.3)' },
          '50%': { boxShadow: '0 0 25px rgba(6, 182, 212, 0.5)' },
        },
      },
    },
  },
  plugins: [],
}
