/**
 * app/layout.tsx — Root Layout
 *
 * This wraps every page in the app. Use it for:
 *   - Global fonts and CSS imports
 *   - Site-wide navigation bar or header
 *   - Providers (e.g., a React context for shared state)
 *
 * HOW NEXT.JS LAYOUTS WORK:
 *   Every page rendered under app/ is wrapped by this component automatically.
 *   The {children} slot is replaced by the current page's content.
 *
 * TODO: Add a <nav> with the FHIRScope logo/title and a link back to home.
 * TODO: Import global CSS here (e.g., import "./globals.css")
 * TODO: Set meaningful <title> and <meta> tags in the metadata export.
 */

import type { Metadata } from "next";

export const metadata: Metadata = {
  // TODO: Update title and description to reflect the app
  title: "FHIRScope",
  description: "Patient medication viewer powered by FHIR",
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="en">
      <body>
        {/* TODO: Add a site-wide header/nav here */}
        <main>{children}</main>
      </body>
    </html>
  );
}
