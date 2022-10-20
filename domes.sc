__config() -> {
   'commands' -> {
     'parabolic <center> <radius> <height> <block>' -> 'parabolic',
     'catenary <center> <radius> <height> <block>' -> ['catenary', 3],
     'catenary <center> <radius> <height> <block> <curve_param>' -> 'catenary',
   },
   'arguments' -> {
     'center' -> {'type' -> 'pos'},
     'radius' -> {'type' -> 'int', 'min' -> 1, 'suggest' -> [10, 32]},
     'height' -> {'type' -> 'int', 'min' -> 1, 'suggest' -> [5, 20]},
     'block' -> {'type' -> 'block'},
     'curve_param' -> {'type' -> 'float', 'suggest' -> [3, 1.7]},
   }
};

parabolic(center, radius, height, blk) -> (
  heightScale = height / radius^2;

  c_for(x=0, x<=radius, x+=1,
    c_for(z=0, z<=radius, z+=1,
      y = round(height - (x^2 + z^2) * heightScale);
      if(y >= 0,
        _setQuadrants(center, [x,y,z], blk)
      )
    )
  );
);

catenary(center, radius, height, blk, curve_param) -> (
  factor = radius / curve_param;

  cat_func(x, z, outer(factor)) -> (
    factor * cosh(sqrt(x^2 + z^2) / factor) - factor;
  );

  heightScale = height / cat_func(radius, 0);

  c_for(x=0, x<=radius, x+=1,
    c_for(z=0, z<=radius, z+=1,
      y = round(height - cat_func(x, z) * heightScale);
      if(y >= 0,
        _setQuadrants(center, [x,y,z], blk)
      )
    )
  );
);

_setQuadrants(center, offset, blk) -> (
  for([1,-1],
    xmod = _;
    for([1, -1],
      zmod = _;
      set(center:0 + offset:0*xmod, center:1 + offset:1, center:2 + offset:2*zmod, blk)
    )
  )
);
