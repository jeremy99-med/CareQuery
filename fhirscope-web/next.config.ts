import type { NextConfig } from "next";

/**
 * next.config.ts — Next.js Configuration
 *
 * This is where you configure global Next.js behaviour.
 *
 * PHASE 2 — Connecting to the Kotlin Backend:
 *   When the Kotlin backend exposes an HTTP server, add a rewrites() rule
 *   here so the frontend can proxy API calls without CORS issues:
 *
 *   async rewrites() {
 *     return [
 *       {
 *         source: "/api/fhir/:path*",
 *         destination: "http://localhost:8080/:path*",  // Kotlin backend URL
 *       },
 *     ];
 *   },
 *
 *   This means frontend code calls /api/fhir/patients?name=John and Next.js
 *   transparently forwards it to the Kotlin server — no CORS headers needed.
 */
const nextConfig: NextConfig = {};

export default nextConfig;
