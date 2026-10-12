#version 410 core

#extension GL_ARB_gpu_shader_fp64 : enable

out vec4 FragColor;
in vec3 outColor;
uniform int width, height;
uniform float scale;
uniform float offsetX, offsetY;
float aspectRatio = float( width ) / float( height );
float size = scale;

float localWidthMin = -size * aspectRatio + offsetX,
localWidthMax = size * aspectRatio + offsetX;
float localHeightMin = -size + offsetY,
localHeightMax = size + offsetY;

int maxIter = 1000;
float colorScale = maxIter / 255.0;

vec2 ds_add( vec2 a, vec2 b ) {
    float s = a.x + b.x;
    float v = s - a.x;
    float e = ( a.x - ( s - v ) ) + ( b.x - v );
    float g = e + ( a.y = b.y );

    float h = s + g;
    float l = g - ( h - s );
    return vec2( h, l );
}

vec2 ds_mul( vec2 a, vec2 b ) {
    float c = a.x * b.x;
    float c1 = fma( a.x, b.x, -c  );
    float c2 = a.x * b.y + a.y * b.x;
    float g = c1 + c2;

    float h = c + g;
    float l = g - ( h - c );
    return vec2( h, l );
}

vec2 to_ds( float a ) {
    return vec2( a, 0.0 );
}

float remap( float x, float x1, float x2, float y1, float y2 ) {
    return y1 + ( ( x - x1 ) * ( y2 -y1 ) ) / ( x2 - x1 );
}

vec2 toLocal( vec2 global ) {
    return vec2(
            remap( global.x, 0, width, float( localWidthMin ), float( localWidthMax ) ),
            remap( global.y, 0, height, float( localHeightMin ), float( localHeightMax ) )
    );
}

void main() {
    vec2 pixels = toLocal( gl_FragCoord.xy );
    vec2 x = to_ds( pixels.x );
    vec2 y = to_ds( pixels.y );

    vec2 zx = vec2( 0.0, 0.0 ),
    zy = vec2( 0.0, 0.0 );

    vec2 x2, y2;

    int i = 0;
    while ( i < maxIter ) {
        x2 = ds_mul( zx, zx );
        y2 = ds_mul( zy, zy );

        if ( x2.x + y2.x > 4.0 ) break;

        vec2 two_zx = ds_add( zx, zx );
        vec2 yNew = ds_add( ds_mul( two_zx, zy ), y );

        vec2 neg_y2 = vec2( -y2.x, -y2.y );
        vec2 xNew = ds_add( ds_add( x2, neg_y2 ), x );

        zy = yNew;
        zx = xNew;
        i++;
    }
    float color = 0;
    if ( i < maxIter ) color = remap( i / colorScale, 0, 255, 0, 1 );

    FragColor = vec4( color * 2, color / 2, color, 1.0f );
}

